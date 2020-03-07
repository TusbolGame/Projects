using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;
using Infra.Common.Models.OrganizationDetail;
using Infra.Common.Models.MessageNotification;

namespace Infra.Common.Models.ServicePack
{
    public class Service
    {

        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
        public string Note { get; set; }
#nullable enable
        public Message? MessageToFeedback { get; set; }
        public Organization? Organization { get; set; }
#nullable disable
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
