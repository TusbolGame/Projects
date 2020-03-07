using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;
using Infra.Common.Models.OrganizationDetail;

namespace Infra.Common.Models.Display
{
    public class UiServiceDesk
    {
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
        public string Note { get; set; }
#nullable enable
        public Organization? Organization { get; set; }
        public int? SecondToShowMessage { get; set; }
#nullable disable
        public string FontColorDefault { get; set; }
        public string FontSizeDefault { get; set; }
        public string LogoOrg { get; set; }
        public string LogoCompany { get; set; }
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
