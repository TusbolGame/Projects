using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Infra.Common.Models.UserRole
{
    public class EmployeeFieldLang
    {
        
        [Key]
        public int Id { get; set; }
#nullable enable
        public string Name { get; set; }
        public Language? Language { get; set; }
#nullable disable
        public bool Delete { get; set; }
    }
}
