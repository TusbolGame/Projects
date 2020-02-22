from django.urls import path
from . import views


urlpatterns = [
    path('', views.index, name='index'),
    # path('register/', views.register, name='register'),
    path('projects/', views.projects, name='projects'),
    path('researchers/', views.researchers, name='researchers'),
    path('submit_work/', views.submit_work, name='submit_work'),
    path('topics/', views.topics, name='topics'),
    path('topic_search/<topic>/', views.topic_search, name='topic_search'),
    path('profile/', views.profile, name='profile'),
]
