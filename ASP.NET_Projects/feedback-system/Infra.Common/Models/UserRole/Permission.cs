using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Infra.Common.Models.UserRole
{
    public class Permission
    {
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
        public string NameGroup { get; set; }
        public string Action { get; set; }
        public string Description { get; set; }
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
