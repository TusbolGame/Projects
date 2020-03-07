using System;
using System.Collections.Generic;
using System.Text;
using System.Globalization;

namespace Infra.Common.exception
{
    public class ArgException : Exception
    {
        public ArgException() : base() { }

        public ArgException(string message) : base(message) { }

        public ArgException(string message, params object[] args)
            : base(String.Format(CultureInfo.CurrentCulture, message, args))
        {
        }
    }
}
