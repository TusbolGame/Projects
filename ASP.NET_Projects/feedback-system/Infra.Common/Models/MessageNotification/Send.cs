using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Infra.Common.Models.MessageNotification
{
    public class Send
    {
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
#nullable enable
        public Message? Message { get; set; }
#nullable disable
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
