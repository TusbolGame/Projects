using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Infra.Common.Models.UserRole
{
    public class Shift
    {
        [Key]
        public int Id { get; set; }
        public FixedShift FixedShift { get; set; }
        public string Day { get; set; }
        public DateTime FromHour { get; set; }
        public DateTime ToHour { get; set; }
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
