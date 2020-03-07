using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Infra.Common.Models.TreatmentPack
{
    public class TreatmentStatus
    {
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
        public bool TicketClosed { get; set; }
        public bool TickedOpened { get; set; }
        public int LocationInList { get; set; }
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
