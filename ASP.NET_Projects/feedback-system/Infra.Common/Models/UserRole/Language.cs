using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Infra.Common.Models.UserRole
{
    public class Language
    {

        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
        public string PictureSrc { get; set; }
#nullable enable
        public bool? Default { get; set; }
#nullable disable
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
