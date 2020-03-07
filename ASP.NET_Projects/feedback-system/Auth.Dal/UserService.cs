using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;
using System.Security.Cryptography;
using Microsoft.EntityFrameworkCore;

using Auth.Common;
using Infra.Common.Models.UserRole;
using Infra.Common.DB;
using System.Threading.Tasks;
using Infra.Common.exception;
using Infra.Bl;
using Infra.Log;
using Auth.Common.Interface;

namespace Auth.Dal
{
    public class UserService : IAuthDal
    {
        private Context _context;

        public UserService(Context context)
        {
            _context = context;
        }

        public async Task<User> CreatUser(User user, string Password)
        {
            if (string.IsNullOrWhiteSpace(Password))
            {
                LogException.Write("Password is required");
                throw new ArgException("Password is required");
            }
            if (_context.Users.Any(x => x.UserName == user.UserName))
            {
                LogException.Write("Username '" + user.UserName + "' is already taken");
                throw new AppException("Username '" + user.UserName + "' is already taken");
            }
            _context.Users.Add(user);
            await _context.SaveChangesAsync();
            return user;
        }

        public async Task<User> PutUser(int Id, User user)
        {
            if (Id != user.Id)
            {
                LogException.Write("Argument Execption: Invalid User");
                throw new ArgumentException("Invalid User");
            }

            _context.Entry(user).State = EntityState.Modified;

            try
            {
                var _user = GetByID(Id);
                await _context.SaveChangesAsync();
                return _user;
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!UserExists(Id))
                {
                    LogException.Write("User doesn't exist");
                    throw new ArgumentException("User doesn't exist");
                }
                else
                {
                    throw;
                }
            }
        }

        public async Task<User> Delete(int Id)
        {
            var user = await _context.Users.FindAsync(Id);
            if (user == null)
            {
                LogException.Write("User doesn't exist");
                throw new ArgumentException("User doesn't exist");
            }

            _context.Users.Remove(user);
            await _context.SaveChangesAsync();
            return user;
        }

        public User GetByID(int ID)
        {
            var user = _context.Users.Find(ID);
            if (user == null)
            {
                return null;
            }
            return _context.Users.Find(ID);
        }
        public User GetByUserName(string username)
        {
            var user = _context.Users.SingleOrDefault(x => x.UserName == username);
            if (user == null)
            {
                return null;
            }
            return user;
        }

        public IEnumerable<User> GetUsers()
        {
            return _context.Users;
        }

        public User Authenticate(string username, string password)
        {
            if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(password))
                return null;

            var user = _context.Users.SingleOrDefault(x => x.UserName == username);

            // check if username exists
            if (user == null)
                return null;

            // check if password is correct
            if (!VerifyPasswordHash(password, user.Password))
                return null;

            // authentication successful
            return user;
        }
        public bool IsActive(User user)
        {
            if (_context.Users.Find(user.Id).IsActive)
            {
                return true;
            }
            else
                return false;
        }

        public bool VerifyPasswordHash(string password, string storedHash)
        {
            if (password == null) 
            {
                LogException.Write("Argument Exception: Password is required");
                throw new ArgumentNullException("password");
            }

            if (string.IsNullOrWhiteSpace(password))
            {
                LogException.Write("Argument Exception: Password cannot be empty or whitespace only string.");
                throw new ArgumentException("Value cannot be empty or whitespace only string.", "password");
            }
                

            string passwordHash;
            CreatePasswordHash(password, out passwordHash);

            if (storedHash != passwordHash) return false;

            return true;
        }

        public void CreatePasswordHash(string password, out string passwordHash)
        {
            if (password == null) 
            {
                LogException.Write("Argument Exception: Password is null");
                throw new ArgumentNullException("Password is null");
            }

            if (string.IsNullOrWhiteSpace(password)) 
            {
                LogException.Write("Argument Exception: Password cannot be empty or whitespace only string.");
                throw new ArgumentException("Value cannot be empty or whitespace only string.", "password");
            }
            
            passwordHash = Hash(Encoding.UTF8.GetBytes(password), "sha256");

        }
        private static string Hash(byte[] input, string algorithm = "sha256")
        {
            using (var hashAlgorithm = HashAlgorithm.Create(algorithm))
            {
                return Convert.ToBase64String(hashAlgorithm.ComputeHash(input));
            }
        }

        public async Task<User> Update(User userParam, string Password = null)
        {
            var user = _context.Users.Find(userParam.Id);

            if (user == null) 
            {
                LogException.Write("Argument Execption: User not found");
                throw new ArgumentException("User not found");
            }
                

            if (!string.IsNullOrWhiteSpace(userParam.UserName) && userParam.UserName != user.UserName)
            {
                if (_context.Users.Any(x => x.UserName == userParam.UserName))
                {
                    LogException.Write("Argument Execption:" + "Username " + userParam.UserName + " is already taken");
                    throw new ArgumentException("Username " + userParam.UserName + " is already taken");
                }
                user.UserName = userParam.UserName;
            }

            // update user properties if provided
            if (!string.IsNullOrWhiteSpace(userParam.FirstName))
                user.FirstName = userParam.FirstName;

            if (!string.IsNullOrWhiteSpace(userParam.LastName))
                user.LastName = userParam.LastName;

            // update password if provided
            if (!string.IsNullOrWhiteSpace(Password))
            {
                string passwordHash;
                CreatePasswordHash(Password, out passwordHash);

                user.Password = passwordHash;
            }

            _context.Users.Update(user);
            _context.SaveChanges();
            return user;
        }

        public bool UserExists(int Id)
        {
            return _context.Users.Any(e => e.Id == Id);
        }
    }
}
