using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Diagnostics;
using System.ComponentModel;
using System.Runtime.InteropServices;
using System.Threading;

namespace INJECT
{
    class Program
    {
        /*
        const int PROCESS_CREATE_THREAD = 0x0002;
        const int PROCESS_QUERY_INFORMATION = 0x0400;
        const int PROCESS_VM_OPERATION = 0x0008;
        const int PROCESS_VM_WRITE = 0x0020;
        const int PROCESS_VM_READ = 0x0010;

        IntPtr pHandle;
        IntPtr Libaddr;
        // used for memory allocation
        const uint MEM_COMMIT = 0x00001000;
        const uint MEM_RESERVE = 0x00002000;
        const uint PAGE_READWRITE = 4;

        [DllImport("user32.dll")]
        static extern IntPtr SetWindowsHookExA(int idHook, IntPtr callback, IntPtr hInstance, uint threadId);

        [DllImport("user32.dll")]
        static extern bool UnhookWindowsHookEx(IntPtr hInstance);

        [DllImport("user32.dll")]
        static extern IntPtr CallNextHookEx(IntPtr idHook, int nCode, int wParam, IntPtr lParam);

        [DllImport("kernel32.dll")]
        static extern IntPtr LoadLibrary(string lpFileName);

        private delegate IntPtr LowLevelKeyboardProc(int nCode, IntPtr wParam, IntPtr lParam);

        [DllImport("kernel32")]
        static extern IntPtr LoadLibraryEx(string lpFileName, IntPtr hFile, uint dwFlags);
        [DllImport("kernel32")]
        static extern bool FreeLibrary(IntPtr hModule);

        [DllImport("kernel32", CharSet = CharSet.Ansi, ExactSpelling = true, SetLastError = true)]
        static extern IntPtr GetProcAddress(IntPtr hModule, string procName);

        [DllImport("user32.dll", SetLastError = true)]
        public static extern bool PostThreadMessage(uint threadId, uint msg, UIntPtr wParam, IntPtr lParam);

        private static IntPtr hhook = IntPtr.Zero;
        private const int WM_NULL = 0x0000;
        private const int DONT_RESOLVE_DLL_REFERENCES = 0x00000002;

        */

        [DllImport("C:\\Inject.dll")]
        static extern IntPtr inject();


        static void Main(string[] args)
        {
            Console.WriteLine("Works now!");
            /*
            Process[] androidEmulator = Process.GetProcessesByName(@"AndroidEmulator");
            int ret = androidEmulator[0].Id;

            //IntPtr windowHandle = androidEmulator[0].MainWindowHandle;


            //windowHandle = OpenProcess(PROCESS_CREATE_THREAD | PROCESS_QUERY_INFORMATION | PROCESS_VM_OPERATION | PROCESS_VM_WRITE | PROCESS_VM_READ, false, proc.Id);

            int tid = androidEmulator[0].Threads[0].Id;

            Console.WriteLine(ret);
            Console.WriteLine(tid);

            if (tid == 0)
            {
                Console.WriteLine("[ FAILED ] Could not get thread ID of the target window.");
                Environment.Exit(12);
            }
            IntPtr hModule = LoadLibraryEx(@"C:\\FF.dll", IntPtr.Zero, 0x00001000);

            IntPtr pAddressOfFunctionToCall = GetProcAddress(hModule, @"AAA");

            hhook = SetWindowsHookExA(3, pAddressOfFunctionToCall, hModule, (uint)tid);

            PostThreadMessage((uint)tid, WM_NULL, (UIntPtr)0, (IntPtr)0);

            Console.ReadLine();
            */
            Console.WriteLine(inject());
        }
        
    }
}
