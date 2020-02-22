from django.db import models
from django.contrib.auth.models import User

# Create your models here.


class Profile(models.Model):

    class Meta:
        verbose_name = 'Researcher'
    STATE_CHOICES = (
        ('1', 'Abia'),
        ('2', 'Adamawa'),
        ('3', 'Akwa-Ibom'),
        ('4', 'Anambra'),
        ('5', 'Bauchi'),
        ('6', 'Bayelsa'),

    )
    GENDER_CHOICES = (
        ('M', 'Male'),
        ('F', 'Female')
    )
    gender = models.CharField(max_length=1, choices=GENDER_CHOICES, default='M')
    full_name = models.CharField(max_length=40, default='')
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    passport = models.ImageField(max_length=300, default='default.jpg', upload_to='passports')
    state = models.CharField(max_length=1, choices=STATE_CHOICES, default='1')
    account_number = models.BigIntegerField(default=0)
    whatsapp_phone_number = models.BigIntegerField(default=0)
    email = models.EmailField(default='')
    qualifications = models.TextField(default='bachelor')

    def __str__(self):
        return self.user.username
