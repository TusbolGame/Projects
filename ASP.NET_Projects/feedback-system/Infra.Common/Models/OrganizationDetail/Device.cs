using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Infra.Common.Models.OrganizationDetail
{
    public class Device
    {
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
        public string Note { get; set; }
#nullable enable
        public ServiceDesk? ServiceDesk { get; set; }
        public Organization? Organization { get; set; }
#nullable disable
        public string ScreenOrientation { get; set; }
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
