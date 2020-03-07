using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;
using Infra.Common.Models.OrganizationDetail;

namespace Infra.Common.Models.UserRole
{
    public class EmployeeField
    {
        
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
#nullable enable
        public bool? FieldGeneral { get; set; }
        public Organization? Organization { get; set; }
#nullable disable
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
