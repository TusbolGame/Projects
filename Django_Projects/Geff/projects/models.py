from django.db import models
from django.contrib.auth.models import User


class Topic(models.Model):
    title = models.CharField(max_length=20, default='')

    def __str__(self):
        return self.title


class Project(models.Model):
    project_title = models.CharField(max_length=30, default='')
    project_topic = models.ForeignKey(Topic, on_delete=models.CASCADE)
    project_description = models.TextField(max_length=160, default='')
    project_preview_file = models.FileField(upload_to='media/preview_files', default='')
    project_main_file = models.FileField(upload_to='media/main_files', default='')
    researcher = models.ForeignKey(User, on_delete=models.CASCADE)
    related_image = models.ImageField(max_length=200, default='', upload_to='media/related_images')

    def __str__(self):
        return self.project_title
