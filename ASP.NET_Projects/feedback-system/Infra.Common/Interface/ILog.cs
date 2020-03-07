using System;
using System.Collections.Generic;
using System.Text;

namespace Infra.Common.Interface
{
    public interface ILog
    {
        void Write(string exception);
    }
}
