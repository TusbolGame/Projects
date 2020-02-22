from django.contrib import admin
from .models import Project, Topic

for i in [Project, Topic]:
    admin.site.register(i)

admin.site.site_header = 'UniSubmit Administration'

admin.site.site_title = 'Admin'
