# Generated by Django 2.2.4 on 2019-08-20 10:11

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('projects', '0006_project_related_image'),
    ]

    operations = [
        migrations.AddField(
            model_name='researcher',
            name='username',
            field=models.CharField(default='', max_length=15),
        ),
    ]
