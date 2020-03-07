using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Infra.Common.Models.UserRole;
using Microsoft.Extensions.Configuration;

namespace Infra.Common.DB
{
    public class Context : DbContext
    {
        protected readonly IConfiguration Configuration;
        public Context(DbContextOptions<Context> options, IConfiguration configuration)
            : base(options)
        {
            Configuration = configuration;
        }

        public DbSet<User> Users { get; set; }
    }
}
