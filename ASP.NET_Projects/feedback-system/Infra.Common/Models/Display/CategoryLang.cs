﻿using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;
using Infra.Common.Models.UserRole;

namespace Infra.Common.Models.Display
{
    public class CategoryLang
    {
        [Key]
        public int Id { get; set; }
        public string Name { get; set; }
        public string Title { get; set; }
#nullable enable
        public Language? Language { get; set; }
#nullable disable
        public bool IsActive { get; set; }
        public bool Delete { get; set; }
    }
}