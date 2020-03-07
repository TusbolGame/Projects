using Infra.Common.Models.UserRole;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace Auth.Common.Interface
{
    public interface IAuthDal
    {
        IEnumerable<User> GetUsers();
        User GetByID(int ID);
        User GetByUserName(string username);
        Task<User> CreatUser(User user, string Password);
        Task<User> Update(User user, string Password = null);
        Task<User> Delete(int Id);
        bool UserExists(int Id);
        Task<User> PutUser(int Id, User user);
        bool IsActive(User user);
        User Authenticate(string username, string password);
        void CreatePasswordHash(string password, out string passwordHash);
        bool VerifyPasswordHash(string password, string storedHash);
    }
}
