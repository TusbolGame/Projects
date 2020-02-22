from django import forms
from .models import Project, Topic
from django.contrib.auth import forms as user_forms
from django.contrib.auth.models import User


class ResearcherForm(forms.ModelForm):
    class Meta:
        model = User
        fields = '__all__'

        widgets = {

            'full_name': forms.TextInput(
                attrs={'class': 'input-group mb-3 form-control', 'placeholder': 'Full Name', }
            ),
            'email': forms.EmailInput(
                attrs={'class': 'input-group mb-3 form-control', 'placeholder': 'Email', }
            ),
            'password': forms.PasswordInput(
                attrs={'class': 'input-group mb-3 form-control', 'placeholder': 'Password', }
            ),
            'passport': forms.FileInput(
                attrs={'class': 'input-group mb-3 custom-file custom-file-input', 'id': 'inputGroupFile01',
                       'aria-describedby': 'inputGroupFileAddon01', 'placeholder': 'Passport', }


            )

        }


class ProjectForm(forms.ModelForm):
    class Meta:
        model = Project
        fields = '__all__'

        widgets = {
            'project_search': forms.TextInput(
                attrs={'class': 'form-control mr-sm-2', 'type': 'search', 'placeholder': 'Search',
                       'aria-label': 'Search', 'name': 'search', }
            ),
            'project_title': forms.TextInput(
                attrs={'class': 'input-group form-control', 'type': 'text', 'placeholder': 'Project Title',
                       'aria-label': 'Search', 'name': 'search', }
            ),
            'project_preview_file': forms.FileInput(
                attrs={'class': 'custom-file-input form-control', 'type': 'file', 'placeholder': 'Preview File',
                       'name': 'file', 'id': 'custom-file', }
            ),
            'project_main_file': forms.FileInput(
                attrs={'class': 'custom-file-input form-control', 'type': 'file', 'placeholder': 'Preview File',
                       'name': 'file', 'id': 'custom-file2', }
            ),
            'project_description': forms.TextInput(
                attrs={'class': 'input-group form-control', 'type': 'text', 'placeholder': 'Project Description',
                       'aria-label': 'Search', 'name': 'search', }
            ),

        }


class TopicForm(forms.ModelForm):
    class Meta:
        model = Topic
        fields = '__all__'

        widgets = {
            'topic_search': forms.TextInput(
                attrs={'class': 'form-control mr-sm-2', 'type': 'search', 'placeholder': 'Search Topic',
                       'aria-label': 'Search', 'name': 'search', }
            ),
        }


class RegisterForm(user_forms.UserCreationForm):
    email = forms.EmailField(label="Email")
    full_name = forms.CharField(label="Full name")
    username = forms.CharField(label="Username")

    class Meta:
        model = User
        fields = '__all__'

        widgets = {
            'email': forms.EmailInput(
                attrs={'class': 'form-control mr-sm-2', 'type': 'text', 'placeholder': 'E-mail'}
            )
        }
