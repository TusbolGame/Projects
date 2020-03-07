using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Infra.Common.Models.OrganizationDetail
{
    public class Area
    {
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
#nullable enable
        public Area? AreaBase { get; set; }
#nullable disable
        public string Note { get; set; }
#nullable enable
        public Organization? Organization { get; set; }
#nullable disable
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
