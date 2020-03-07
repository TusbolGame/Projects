using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;
using Infra.Common.Models.ServicePack;
using Infra.Common.Models.OrganizationDetail;
using Infra.Common.Models.TreatmentPack;

namespace Infra.Common.Models.MessageNotification
{
    public class ModeToSend
    {
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
#nullable enable
        public Feedback? Feedback { get; set; }
        public Area? Area { get; set; }
        public TreatmentStatus? TreatmentStatus { get; set; }
        public int? AboveSeveralFeedback { get; set; }
        public Organization? Organization { get; set; }
#nullable disable
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
