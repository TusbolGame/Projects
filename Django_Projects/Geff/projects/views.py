from django.shortcuts import render, redirect
from .forms import ResearcherForm, ProjectForm
from .models import Project, Topic
from django.contrib.auth import authenticate, login
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.contrib import messages


def topics(request):
    context = {
        'Topics': Topic.objects.all,
        'Projects': Project.objects.all,
    }
    return render(request, 'projects/topics.html', context)


def topic_search(request, topic):
    topic_ = Topic.objects.get(pk=topic)
    context = {
        'Topics': Topic.objects.all,
        'topic': topic_,
        'projects': Project.objects.filter(project_topic=topic_),
    }
    return render(request, 'projects/projects.html', context)


@login_required()
def profile(request):
    context = {
        'Topics': Topic.objects.all,
        'projects': Project.objects.filter(researcher=request.user)
    }
    return render(request, 'users/profile.html', context=context)


def index(request):
    context = {
        'Projects': Project.objects.all,
        'Topics': Topic.objects.all,
        'project_form': ProjectForm(),

    }
    return render(request, 'projects/GeffHome.html', context)


def register(request):
    if request.method == 'POST':
        form = UserCreationForm(request.POST)

        if form.is_valid:
            form.save()
            username = form.cleaned_data['username']
            password = form.cleaned_data['password1']
            email = form.cleaned_data['email']
            user = authenticate(username=username, password=password, email=email)
            login(request, user)
            return redirect('index')

        else:
            return redirect('register')

    else:
        form = UserCreationForm()

    context = {
        'form': form,
        'researcher_form': ResearcherForm(),
        'Topics': Topic.objects.all,
    }
    return render(request, 'projects/register.html', context)


def projects(request):
    context = {
        'projects': Project.objects.all,
        'Topics': Topic.objects.all,
    }
    return render(request, 'projects/projects.html', context)


def researchers(request):
    context = {
        'Researchers': User.objects.all,
        'Topics': Topic.objects.all,
        'Projects': Project.objects.all,
    }

    return render(request, 'projects/researchers.html', context)


@login_required
def submit_work(request):

    researcher = User
    if request.method == 'POST':
        form = ProjectForm(request.POST)

        if form.is_valid:
            form.save()
            project_description = form.cleaned_data['project_description']
            project_preview_file = form.cleaned_data['project_preview_file']
            project_main_file = form.cleaned_data['project_main_file']
            project_title = form.cleaned_data['project_title']

            project = authenticate(project_description=project_description, project_preview_file=project_preview_file,
                                   project_main_file=project_main_file, project_title=project_title,
                                   researcher=researcher)
            Project.save(project)
            return redirect('index')

        else:
            messages.error(request, 'Data validation error!')
            return redirect('register')

    else:
        form = UserCreationForm()

    context = {
        'Topics': Topic.objects.all,
        'project_form': ProjectForm,

    }
    return render(request, 'projects/submit_work.html', context)
