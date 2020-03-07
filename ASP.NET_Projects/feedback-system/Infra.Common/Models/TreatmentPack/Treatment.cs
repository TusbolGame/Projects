using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;
using Infra.Common.Models.ServicePack;
using Infra.Common.Models.OrganizationDetail;
using Infra.Common.Models.UserRole;

namespace Infra.Common.Models.TreatmentPack
{
    public class Treatment
    {
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
        public string Note { get; set; }
        public DateTime TimeOpened { get; set; }
        public DateTime TimeClosed { get; set; }
        public User UserClosed { get; set; }
        public User UserEdited { get; set; }
        public Service Service { get; set; }
#nullable enable
        public Feedback? Feedback { get; set; }
        public Area? AreaBase { get; set; }
#nullable disable
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
