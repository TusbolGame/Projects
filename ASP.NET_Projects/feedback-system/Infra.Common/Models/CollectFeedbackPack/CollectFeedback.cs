using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;
using Infra.Common.Models.ServicePack;
using Infra.Common.Models.OrganizationDetail;
using Infra.Common.Models.TreatmentPack;

namespace Infra.Common.Models.CollectFeedbackPack
{
    public class CollectFeedback
    {
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
#nullable enable
        public Feedback? Feedback { get; set; }
        public Area? AreaBase { get; set; }
        public ServiceDesk? ServiceDeskOpened { get; set; }
        public Area? AreaOpened { get; set; }
#nullable disable
        public DateTime TimeOpened { get; set; }
        public string LanguageOpened { get; set; }
#nullable enable
        public Treatment? Treatment { get; set; }
#nullable disable
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
