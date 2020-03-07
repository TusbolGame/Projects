using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Infra.Common.Models.UserRole
{
    public class FixedShift
    {
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
        public Role Role { get; set; }
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
