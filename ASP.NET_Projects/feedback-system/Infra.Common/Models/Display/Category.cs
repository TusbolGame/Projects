using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;
using Infra.Common.Models.ServicePack;
using Infra.Common.Models.OrganizationDetail;

namespace Infra.Common.Models.Display
{
    public class Category
    {
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
        public string Note { get; set; }
#nullable enable
        public Feedback? Feedback { get; set; }
        public Area? Area { get; set; }
        public int? MaxInRow { get; set; }
        public int? MaxInColumn { get; set; }
#nullable disable
        public string FontColor_Name { get; set; }
#nullable enable
        public int? FontSize_Name { get; set; }
#nullable disable
        public string FontColor_Title { get; set; }
#nullable enable
        public int? FontSize_Title { get; set; }
        public Category? CategoryBase { get; set; }
        public UiServiceDesk? UiServiceDesk { get; set; }
#nullable disable
        public string PictureSrc { get; set; }
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}
