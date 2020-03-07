using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;
using Infra.Common.Models.OrganizationDetail;
using Infra.Common.Models.MessageNotification;

namespace Infra.Common.Models.UserRole
{
    public class User
    {
        [Key]
        public int Id { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string UserName { get; set; }
        public string Password { get; set; }
        public string PinCode { get; set; }
        public string Comment { get; set; }
#nullable enable
        public Organization? Organization { get; set; }
        public Language? Language { get; set; }
        public SendingOptions? SendingOptions { get; set; }
        //public string? Token { get; set; }
#nullable disable
        public bool IsActive { get; set; }
        public bool Delete { get; set; }      

    }
    public class AuthenticateModel
    {
#nullable disable
        [Required(ErrorMessage = "UserName is required")]
        public string Username { get; set; }
        [Required(ErrorMessage = "Password is required")]
        public string Password { get; set; }
    }
}
