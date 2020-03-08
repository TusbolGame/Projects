// dllmain.cpp : Defines the entry point for the DLL application.
#include "pch.h"
#include <Windows.h>
#include <iostream>
#include <string>
#include <TlHelp32.h>
#include <vector>

using namespace std;

int GetProcessId(std::wstring targetProcessName) {
	vector<DWORD> pids;
	HANDLE snap = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);

	PROCESSENTRY32W entry;
	entry.dwSize = sizeof entry;

	if (!Process32FirstW(snap, &entry)) {
		return 0;
	}

	do {
		if (std::wstring(entry.szExeFile) == targetProcessName) {
			pids.emplace_back(entry.th32ProcessID);
		}
	} while (Process32NextW(snap, &entry));

	for (int i(0); i < pids.size(); ++i) {
		std::cout << pids[i] << std::endl;
	}
	return pids[0];
}

BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
                     )
{
    switch (ul_reason_for_call)
    {
    case DLL_PROCESS_ATTACH:
    case DLL_THREAD_ATTACH:
    case DLL_THREAD_DETACH:
    case DLL_PROCESS_DETACH:
        break;
    }
    return TRUE;
}

extern "C" __declspec (dllexport) int inject()
{
	int ret = GetProcessId(L"AndroidEmulator.exe");
	cout << ret << endl;
	HWND hwnd = ::GetTopWindow(0);
	while (hwnd)
	{
		DWORD pid;
		DWORD dwTheardId = ::GetWindowThreadProcessId(hwnd, &pid);
		if (pid == ret)
		{
			break;
		}
		hwnd = ::GetNextWindow(hwnd, GW_HWNDNEXT);
	}
	DWORD pid = ret;
	DWORD tid = GetWindowThreadProcessId(hwnd, &pid);
	cout << tid << endl;
	if (tid == NULL) {
		cout << "[ FAILED ] Could not get thread ID of the target window." << endl;
		system("pause");
		return EXIT_FAILURE;
	}
	HMODULE dll = LoadLibraryEx(L"C:\\APIManager.dll", NULL, DONT_RESOLVE_DLL_REFERENCES);
	if (dll == NULL) {
		cout << "[ FAILED ] The DLL could not be found." << endl;
		system("pause");
		return EXIT_FAILURE;
	}
	HOOKPROC addr = (HOOKPROC)GetProcAddress(dll, "AAA");
	if (addr == NULL) {
		cout << "[ FAILED ] The function was not found." << endl;
		system("pause");
		return EXIT_FAILURE;
	}
	HHOOK handle = SetWindowsHookEx(WH_GETMESSAGE, addr, dll, tid);
	if (handle == NULL) {
		cout << "[ FAILED ] Couldn't set the hook with SetWindowsHookEx." << endl;
		system("pause");
		return EXIT_FAILURE;
	}
	PostThreadMessage(tid, WM_NULL, NULL, NULL);
	cout << "[ OK ] Hook set and triggered." << endl;
	cout << "[ >> ] Press any key to unhook (This will unload the DLL)." << endl;
	system("pause > nul");
	BOOL unhook = UnhookWindowsHookEx(handle);
	cout << "Here" << endl;
	if (unhook == FALSE) {
		cout << "[ FAILED ] Could not remove the hook." << endl;
		system("pause");
		return EXIT_FAILURE;
	}
	cout << "[ OK ] Done. Press any key to exit." << endl;
	system("pause > nul");
	return EXIT_SUCCESS;
}

