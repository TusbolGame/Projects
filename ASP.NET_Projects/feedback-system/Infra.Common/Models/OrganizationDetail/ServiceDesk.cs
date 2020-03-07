using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;
using Infra.Common.Models.Display;

namespace Infra.Common.Models.OrganizationDetail
{
    public class ServiceDesk
    {

        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
        public string Detail { get; set; }
#nullable enable
        public Area? Area { get; set; }
        public UiServiceDesk? UiServiceDesk { get; set; }
#nullable disable
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
