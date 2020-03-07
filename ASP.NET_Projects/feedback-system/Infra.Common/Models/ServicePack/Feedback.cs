using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;
using Infra.Common.Models.MessageNotification;

namespace Infra.Common.Models.ServicePack
{
    public class Feedback
    {
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
        public string Note { get; set; }
        public string Color { get; set; }
        public string PictureSrc { get; set; }
#nullable enable
        public int? Grade { get; set; }
        public Service? Service { get; set; }
        public Message? MessageToFeedback { get; set; }
#nullable disable
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
