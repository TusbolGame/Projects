using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

using Infra.Common.Models.UserRole;
using System.Security.Cryptography;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;
using System.Text;
using System.Security.Claims;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using Microsoft.Extensions.Configuration;
using Microsoft.AspNetCore.Authorization;
using Microsoft.Extensions.Options;

using Auth.Common;
using Infra.Common.exception;
using Infra.Bl;
using Auth.Common.Interface;
using Infra.Log;

namespace Auth.Controllers
{
    [Authorize]
    [ApiController]
    [Route("[controller]")]
    public class AuthController : ControllerBase
    {
        private readonly IAuthDal _authDal;
        private IConfiguration _config;

        private readonly int expDays = 4;
        private readonly int expHours = 8;
        private int expireDuration;

        public AuthController(IAuthDal authDal, IConfiguration config)
        {
            _authDal = authDal;
            _config = config;
            expireDuration = expDays * 24 * 3600 + expHours * 3600;
        }

        [HttpGet]
        public IActionResult GetUsers()
        {
            var username = HttpContext.User.Identity.Name;
            var user = _authDal.GetByUserName(username.ToString());

            var header = HttpContext.Request.Headers;

            string accessToken = header["Authorization"].ToString().Substring(7);

            if (accessToken == null)
            {
                return BadRequest(new { message = "Not Authrized" });
            }

            if (user == null)
            {
                return BadRequest(new { message = "Username does not exist" });
            }

            return Ok(user);
        }

        // GET: api/Auth/5
        [HttpGet("{id}")]
        public IActionResult GetUser(int id)
        {
            var user = _authDal.GetByID(id);

            if (user == null)
            {
                return NotFound();
            }

            return Ok(user);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> PutUser(int id, User user)
        {
            var _user = await _authDal.PutUser(id, user);
            return Ok(_user);
        }

        [AllowAnonymous]
        [HttpPost("login")]
        public async Task<ActionResult<User>> Login(AuthenticateModel attempt)
        {
            var user = _authDal.Authenticate(attempt.Username, attempt.Password);

            if (user == null)
                return BadRequest(new { message = "Username or password is incorrect" });

            if (user.IsActive == false)
            {
                return BadRequest(new { message = "Account is disabled" });
            }
            if (user.Delete == true)
            {
                return BadRequest(new { message = "Account is disabled" });
            }

            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_config["Jwt:Key"]));
            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);

            var claims = new[]
            {
                new Claim(JwtRegisteredClaimNames.Sub, user.Id.ToString()),
                new Claim(JwtRegisteredClaimNames.GivenName, user.FirstName),
                new Claim(JwtRegisteredClaimNames.FamilyName, user.LastName),
                new Claim(JwtRegisteredClaimNames.UniqueName, user.UserName),
                new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString())
            };

            var tokenBuilder = new JwtSecurityToken(
                issuer: _config["Jwt:Issuer"],
                audience: _config["Jwt:Issuer"],
                claims,
                expires: DateTime.Now.AddSeconds(expireDuration),
                signingCredentials: credentials
            ); ;
            var token = new JwtSecurityTokenHandler().WriteToken(tokenBuilder);

            return Ok(new
            {
                Id = user.Id,
                Username = user.UserName,
                FirstName = user.FirstName,
                LastName = user.LastName,
                Token = token,
            });
        }

        [AllowAnonymous]
        [HttpPost("register")]
        public async Task<ActionResult<User>> PostUser(User user)
        {
            string password = user.Password;
            string passwordHash;


            _authDal.CreatePasswordHash(password, out passwordHash);

            user.Password = passwordHash;

            try
            {
                var _user = await _authDal.CreatUser(user, passwordHash);
                return Ok(_user);
            }
            catch (ArgumentException ex)
            {
                LogException.Write("Argument Execption:" + ex.Message);
                return BadRequest(new
                {
                    message = ex.Message
                });
            }
        }

        // DELETE: Auth/5
        [HttpDelete("{id}")]
        public async Task<ActionResult<User>> DeleteUser(int id)
        {
            var user = await _authDal.Delete(id);
            return user;
        }

        private bool UserExists(int id)
        {
            return _authDal.UserExists(id);
        }
    }
}
