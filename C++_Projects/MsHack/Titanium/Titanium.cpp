// Titanium.cpp : Defines the initialization routines for the DLL.
//

#include "pch.h"
#include "framework.h"
#include "Titanium.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

//
//TODO: If this DLL is dynamically linked against the MFC DLLs,
//		any functions exported from this DLL which call into
//		MFC must have the AFX_MANAGE_STATE macro added at the
//		very beginning of the function.
//
//		For example:
//
//		extern "C" BOOL PASCAL EXPORT ExportedFunction()
//		{
//			AFX_MANAGE_STATE(AfxGetStaticModuleState());
//			// normal function body here
//		}
//
//		It is very important that this macro appear in each
//		function, prior to any calls into MFC.  This means that
//		it must appear as the first statement within the
//		function, even before any object variable declarations
//		as their constructors may generate calls into the MFC
//		DLL.
//
//		Please see MFC Technical Notes 33 and 58 for additional
//		details.
//

// CTitaniumApp

// CTitaniumApp construction


using namespace std;

CTitaniumApp::CTitaniumApp()
{
	// TODO: add construction code here,
	// Place all significant initialization in InitInstance
	char *process = "MapleStory.exe";
	char pattern[] = "\x55\x8B\xEC\x6A\xFF\x68\x00\x00\x00\x00\x64\xA1\x00\x00\x00\x00\x50\x81\xEC\x00\x00\x00\x00\xA1\x00\x00\x00\x00\x33\xC5\x89\x00\x00\x53\x56\x57\x50\x8D\x00\x00\x64\xA3\x00\x00\x00\x00\x89\x00\x00\xC7\x00\x00\x00\x00\x00\x00\x6A\x01\x8B\x4D\x00";		// The pattern we are looking for
	char mask[] = "xxxxxx????xxxxxxxxx????x????xxx??xxxxx??xx????x??x??????xxxx?";

	size_t origsize = strlen(process) + 1;
	const size_t newsize = 100;
	size_t convertedChars = 0;
	wchar_t procName[newsize];
	mbstowcs_s(&convertedChars, procName, origsize, process, _TRUNCATE);

	m_pID = GetPID(procName);																													// Obtain the process ID of the above process
	if (!m_pID) {																																		// If we can't get a valid process ID
		std::cout << "Could not find process" << std::endl;																							// Let the user know
		std::cin.get();																								// Return 1 and exit the program
	}

	m_pHandle = OpenProcess(PROCESS_VM_OPERATION | PROCESS_VM_READ | PROCESS_VM_WRITE, false, m_pID);											// Obtain a handle to the process
	if (!m_pHandle) {																																	// If we can't get a handle to the process
		std::cout << "Could not obtain handle" << std::endl;																						// Let the user know
		std::cin.get();																																			// Return 1 and exit the program
	}
	m_address = ExternalAoBScan(m_pHandle, m_pID, procName, pattern, mask);																			// Run the AoB scan and store the returned address in the address varaible
	if (m_address) {																																	// If it's not NULL
		std::cout << "Address: " << std::hex << m_address << std::endl;																				// Print it to the screen
	}
	else {																																			// If it is null
		std::cout << "No address" << std::endl;																										// Let the user know
	}


	char t_pattern[] = "\xB8\x00\x00\x00\x00\x8B\x4D\x00\x00\x00\x00\x00\x00\x00\x00\x59\x5F\x5E\x8B\xE5\x5D\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x2D";		// The pattern we are looking for
	char t_mask[] = "x????xx????????xxxxxx????????????????x";

	size_t t_origsize = strlen(process) + 1;
	const size_t t_newsize = 100;
	size_t t_convertedChars = 0;
	wchar_t t_procName[t_newsize];
	mbstowcs_s(&t_convertedChars, t_procName, t_origsize, process, _TRUNCATE);

	t_pID = GetPID(t_procName);																													// Obtain the process ID of the above process
	if (!t_pID) {																																		// If we can't get a valid process ID
		std::cout << "Could not find process" << std::endl;																							// Let the user know
		std::cin.get();																								// Return 1 and exit the program
	}

	t_pHandle = OpenProcess(PROCESS_VM_OPERATION | PROCESS_VM_READ | PROCESS_VM_WRITE, false, t_pID);											// Obtain a handle to the process
	if (!t_pHandle) {																																	// If we can't get a handle to the process
		std::cout << "Could not obtain handle" << std::endl;																						// Let the user know
		std::cin.get();																																			// Return 1 and exit the program
	}
	//t_address = ExternalAoBScan(t_pHandle, t_pID, t_procName, t_pattern, t_mask);																			// Run the AoB scan and store the returned address in the address varaible
	if (t_address) {																																	// If it's not NULL
		std::cout << "Address: " << std::hex << t_address << std::endl;																				// Print it to the screen
	}
	else {																																			// If it is null
		std::cout << "No address" << std::endl;																										// Let the user know
	}

	
	char H1_pattern[] = "\x74\x00\x83\xBF\x00\x00\x00\x00\x00\x89\xB7\x00\x00\x00\x00\x7F\x04\x6A\x00";		// The pattern we are looking for
	char H1_mask[] = "x?xx?????xx????xxx?";

	char H2_pattern[] = "\x7F\x00\x6A\x00\xEB\x00\x8B\x0D\x00\x00\x00\x00\xE8\x00\x00\x00\x00";		// The pattern we are looking for
	char H2_mask[] = "x?x?x?Xx????x????";

	char H3_pattern[] = "\x75\x00\x53\x6A\x00\x6A\x00\x8B\xCF\xE8\x00\x00\x00\x00\x5F";		// The pattern we are looking for
	char H3_mask[] = "x?xx?x?xXX????x";

	//H1_address = ExternalAoBScan(m_pHandle, m_pID, t_procName, H1_pattern, H1_mask);																			// Run the AoB scan and store the returned address in the address varaible
	if (H1_address) {																																	// If it's not NULL
		std::cout << "Address: " << std::hex << t_address << std::endl;																				// Print it to the screen
	}
	else {																																			// If it is null
		std::cout << "No address" << std::endl;																										// Let the user know
	}
	//H2_address = ExternalAoBScan(m_pHandle, m_pID, t_procName, H2_pattern, H2_mask);																			// Run the AoB scan and store the returned address in the address varaible
	if (H2_address) {																																	// If it's not NULL
		std::cout << "Address: " << std::hex << t_address << std::endl;																				// Print it to the screen
	}
	else {																																			// If it is null
		std::cout << "No address" << std::endl;																										// Let the user know
	}
	//H3_address = ExternalAoBScan(m_pHandle, m_pID, t_procName, H3_pattern, H3_mask);																			// Run the AoB scan and store the returned address in the address varaible
	if (H3_address) {																																	// If it's not NULL
		std::cout << "Address: " << std::hex << t_address << std::endl;																				// Print it to the screen
	}
	else {																																			// If it is null
		std::cout << "No address" << std::endl;																										// Let the user know
	}

}


// CPU Hacks
void CTitaniumApp::setMSCRCBypass()
{
	char newByte[] = "\x33\xC0\xC3";
	WriteProcessMemory(m_pHandle, (BYTE*)m_address, newByte, sizeof(newByte), nullptr);
}
void CTitaniumApp::unsetMSCRCBypass()
{
	char newByte[] = "\x55\x8B\xEC";
	WriteProcessMemory(m_pHandle, (BYTE*)m_address, newByte, sizeof(newByte), nullptr);
}
void CTitaniumApp::setNoCatchBreath()
{
	char newByte[] = "\x78";
	auto address = "010F1C99";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x010F1C99), newByte, sizeof(newByte), nullptr);
}
void CTitaniumApp::unsetNoCatchBreath()
{
	char newByte[] = "\x79";
	auto address = "010F1C99";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x010F1C99), newByte, sizeof(newByte), nullptr);
}
void CTitaniumApp::setSkipVideoLogo()
{
	char newByte[] = "\x90\x90\x90\x90\x90\x90";
	auto address = "01802D64";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x01802D64), newByte, sizeof(newByte), nullptr);
}
void CTitaniumApp::unsetSkipVideoLogo()
{
	char newByte[] = "\x0F\x84\x66\x17\x00\x00";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x01802D64), newByte, sizeof(newByte), nullptr);
}
void CTitaniumApp::setNoFadeInOut()
{
	char newByte[] = "\x90\xE9";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x01BE36EF), newByte, sizeof(newByte), nullptr);
	char newByte1[] = "\x90\xE9";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x01BE3D4F), newByte1, sizeof(newByte1), nullptr);
}
void CTitaniumApp::unsetNoFadeInOut()
{
	char newByte[] = "\x0F\x84\x13\x06\x00\x00";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x01BE36EF), newByte, sizeof(newByte), nullptr);
	char newByte1[] = "\x0F\x84\x19\x0D\x00\x00";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x01BE3D4F), newByte1, sizeof(newByte1), nullptr);
}
bool CTitaniumApp::Hook(void * toHook, void * ourFunct, int len)
{
	if (len < 5)
	{
		return false;
	}
	DWORD curProtection;
	VirtualProtect(toHook, len, PAGE_EXECUTE_READWRITE, &curProtection);

	memset(toHook, 0x90, len);
	//for (DWORD x = 0x5; x < len; x++)
	//{
		//*(toHook + x) = 0x90;
	//}

	DWORD relateiveAddress = (DWORD)ourFunct - (DWORD)toHook - 5;

	*(BYTE*)toHook = 0xE9;
	*(DWORD*)((DWORD)toHook + 1) = relateiveAddress;

	DWORD temp;
	VirtualProtect(toHook, len, curProtection, &temp);
	return true;
}
DWORD jmpBackAddy;
void _declspec(naked) ourFunct()
{
	__asm {
		mov eax, 0x00000000
		jmp [jmpBackAddy]
	}
}
void CTitaniumApp::PlaceJMP(BYTE *Address, DWORD jumpTo, DWORD length)
{
	DWORD dwOldProtect, dwBkup, dwRelAddr;

	VirtualProtect(Address, length, PAGE_EXECUTE_READWRITE, &dwOldProtect);
	dwRelAddr = (DWORD)(jumpTo - (DWORD)Address) - 5;

	*Address = 0xE9;
	*((DWORD *)(Address + 0x1)) = dwRelAddr;

	for (DWORD x = 0x5; x < length; x++)
	{
		*(Address + x) = 0x90;
	}
	VirtualProtect(Address, length, dwOldProtect, &dwBkup);
}
void CTitaniumApp::setNoUITransparency()
{
	//int hookLength = 6;
	//DWORD hookAddress = t_address;
	//jmpBackAddy = hookAddress + hookLength;

	//int *value = *(DWORD*)t_address;

	//Hook((void *)hookAddress, ourFunct, hookLength);

	char newByte[] = "\xB8\x00\x00\x00\x00";
	WriteProcessMemory(t_pHandle, (LPVOID)(t_address), newByte, sizeof(newByte), nullptr);
}
void CTitaniumApp::unsetNoUITransparency()
{
	char newByte[] = "\xB8\x01\x00\x00\x00";
	WriteProcessMemory(t_pHandle, (LPVOID)(t_address), newByte, sizeof(newByte), nullptr);
}
void CTitaniumApp::setNoLootAnimation()
{
	char newByte[] = "\x0F\x8C";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x01056986), newByte, sizeof(newByte), nullptr);
}
void CTitaniumApp::unsetNoLootAnimation()
{
	char newByte[] = "\x0F\x8D";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x01056986), newByte, sizeof(newByte), nullptr);
}
void CTitaniumApp::setNoMobReaction() 
{
	char newByte[] = "\xC2\x74\x00";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x0189DE10), newByte, sizeof(newByte), nullptr);
}
void CTitaniumApp::unsetnoMobReaction()
{
	char newByte[] = "\x55\x8B\xEC";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x0189DE10), newByte, sizeof(newByte), nullptr);
}
void CTitaniumApp::setNoMultiKill()
{
	char newByte[] = "\xC3\x66\x66\x65\x63\x74\x2F\x42";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x03521D9C), newByte, sizeof(newByte) - 1, nullptr);
}
void CTitaniumApp::unsetNoMultiKill()
{
	char newByte[] = "\x45\x66\x66\x65\x63\x74\x2F\x42";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x03521D9C), newByte, sizeof(newByte) - 1, nullptr);
}
void CTitaniumApp::setNoMobSpawn()
{	
	char newByte[] = "\x90\x90\x90\x90\x90\x90";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x018AE434), newByte, sizeof(newByte), nullptr);
	char newByte1[] = "\xC3";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x0189F270), newByte1, sizeof(newByte1) - 1, nullptr);
}
void CTitaniumApp::unsetNoMobSpawn()
{	
	char newByte[] = "\x0F\x8E\x13\x02\x00\x00";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x018AE434), newByte, sizeof(newByte), nullptr);
	char newByte1[] = "\x55";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x0189F270), newByte1, sizeof(newByte1) - 1, nullptr);
}


// Character Hack
void CTitaniumApp::setUndetectedMissGodMode()
{
	double newValue = -50000000;
	WriteProcessMemory(m_pHandle, (LPVOID)(0x032D5D90), &newValue, sizeof(newValue), nullptr);
}

void CTitaniumApp::unsetUndetectedMissGodMode()
{
	double newValue = 50000000;
	WriteProcessMemory(m_pHandle, (LPVOID)(0x032D5D90), &newValue, sizeof(newValue), nullptr);
}

void CTitaniumApp::setBossGodMode()
{
	char newByte[] = "\xB8\x01\x00\x00\x00";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x014289A6), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::unsetBossGodMode()
{
	char newByte[] = "\xE8\x45\x57\x58\xFF";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x014289A6), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::setSpecialBossDamage()
{
	char newByte[] = "\x90\x90";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x018D9B4F), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::unsetSpecialBossDamage()
{
	char newByte[] = "\x85\xC0";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x018D9B4F), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::setAutoHPMP()
{

}

void CTitaniumApp::unsetAutoHPMP()
{

}

void CTitaniumApp::setUnlimitedFlash()
{
	char newByte[] = "\xEB";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x02966112), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::unsetUnlimitedFlash()
{
	char newByte[] = "\x7C";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x02966112), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::setGenericNoDelay()
{
	char newByte[] = "\x8A";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x0285899B), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::unsetGenericNoDelay()
{
	char newByte[] = "\x8B";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x0285899B), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::setUnlimitedAttack()
{
	char newByte[] = "\xEB";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x02939CD1), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::unsetUnlimitedAttack()
{
	char newByte[] = "\x7E";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x02939CD1), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::setPerfectStance()
{
	char newByte[] = "\x90\x90\x90";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x02841292), newByte, sizeof(newByte) - 1, nullptr);
	char newByte1[] = "\xEB";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x0284129C), newByte1, sizeof(newByte1) - 1, nullptr);
}

void CTitaniumApp::unsetPerfectStance()
{
	char newByte[] = "\x83\x7D\x0C\x00\x75\x06";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x02841292), newByte, sizeof(newByte) - 1, nullptr);
	char newByte1[] = "\x74";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x0284129C), newByte1, sizeof(newByte1) - 1, nullptr);
}

void CTitaniumApp::setNoDelayMining()
{
	char newByte1[] = "\x90\x90";
	WriteProcessMemory(m_pHandle, (BYTE*)H1_address, newByte1, sizeof(newByte1) - 1, nullptr);
	char newByte2[] = "\xEB";
	WriteProcessMemory(m_pHandle, (BYTE*)H2_address, newByte2, sizeof(newByte2) - 1, nullptr);
	char newByte3[] = "\x90\x90";
	WriteProcessMemory(m_pHandle, (BYTE*)H3_address, newByte3, sizeof(newByte3) - 1, nullptr);

}

void CTitaniumApp::unsetNoDelayMining()
{
	char newByte1[] = "\x74\x36";
	WriteProcessMemory(m_pHandle, (BYTE*)H1_address, newByte1, sizeof(newByte1) - 1, nullptr);
	char newByte2[] = "\x7F\x04";
	WriteProcessMemory(m_pHandle, (BYTE*)H2_address, newByte2, sizeof(newByte2) - 1, nullptr);
	char newByte3[] = "\x75\x12";
	WriteProcessMemory(m_pHandle, (BYTE*)H3_address, newByte3, sizeof(newByte3) - 1, nullptr);
}

void CTitaniumApp::setInstantDrop()
{
	char newByte[] = "\xF2\x0F\x5E";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x0139687F), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::unsetInstantDrop()
{
	char newByte[] = "\xF2\x0F\x59";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x0139687F), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::setInfiniteBuff()
{
	char newByte[] = "\xE9\x3C\x02\xA1\x0C";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x02C0FDBF), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::unsetInfiniteBuff()
{
	char newByte[] = "\x3D\xBA\x01\x00\x00";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x02C0FDBF), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::setConstantBlazing()
{
	char newByte[] = "\x90\x90\x90\x90\x90\x90";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x016BF79E), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::unsetConstantBlazing()
{
	char newByte[] = "\x0F\x8E\xFE\x00\x00\x00";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x016BF79E), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::setFreezeBlazing()
{
	char newByte[] = "\x90\x90\x90\x90\x90";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x016BDA28), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::unsetFreezeBlazing()
{
	char newByte[] = "\xE8\xE3\x25\x6A\xFF";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x016BDA28), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::setNoBlazingExtinction()
{
	char newByte[] = "\xC2\x10\x00";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x0167E890), newByte, sizeof(newByte) - 1, nullptr);
}

void CTitaniumApp::unsetNoBlazingExtinction()
{
	char newByte[] = "\x55\x8B\xEC";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x0167E890), newByte, sizeof(newByte) - 1, nullptr);
}






// General Hacks

DWORD get_field = 0x0145E0A0;
DWORD CUserRemote__Update = 0x029B6950;
DWORD TSingletonCUserPool = 0x03A82D24;
DWORD TSingletonCWvsContext = 0x03A47F6C;
DWORD CField__SendTransferChannelRequest = 0x01424310;
void _declspec(naked) CheckChannelCount()
{
	__asm {
		/*
		mov eax, [TSingletonCWvsContext]
		mov eax, [eax + 0x000021CC]
		inc eax
		cmp eax, 14
		jb CheckRemoteUserCount
		xor eax, eax
		jmp CheckRemoteUserCount
		*/
	}
}
/*
void _declspec(naked) CheckRemoteUserCount()
{
	__asm {
		push ecx
		mov ecx, [TSingletonCUserPool]
		mov ecx, [ecx + 0x0BC + 0xC] // 0BC (??) [Was 0C+C] (Needs Update)
		cmp ecx, [RemoteUsers]
		jae ChangeChannel
		jmp End
	}
}
void _declspec(naked) ChangeChannel()
{
	__asm {
		push eax
		call get_field
		mov ecx, eax
		call CField__SendTransferChannelRequest
	}
}
void _declspec(naked) End()
{
	__asm {
		pop ecx
		push ebp
		mov ebp, esp
		push - 01
		jmp Return
	}
}
void _declspec(naked) Return()
{
	__asm {

		ret
	}
}
*/
void CTitaniumApp::setAutoCC()
{
	//CheckChannelCount();
	/*
	__asm {
	
		mov eax, [TSingletonCWvsContext]
		mov eax, [eax + 0x000021CC]
		inc eax
		cmp eax, 14 
		jb CheckRemoteUserCount
		xor eax, eax
		jmp CheckRemoteUserCount
	}
	*/
}
void CTitaniumApp::unsetAutoCC()
{

}
void CTitaniumApp::setPetLoot()
{

}
void CTitaniumApp::unsetPetLoot()
{

}
void CTitaniumApp::setInfiniteFamiliar()
{
	char newByte[] = "\xC3";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x013E62C0), newByte, sizeof(newByte) - 1, nullptr);
}
void CTitaniumApp::unsetInfiniteFamiliar()
{
	char newByte[] = "\x55";
	WriteProcessMemory(m_pHandle, (LPVOID)(0x013E62C0), newByte, sizeof(newByte) - 1, nullptr);
}
void CTitaniumApp::setDisableChatLangCheck()
{
	//char newByte[] = "\x83\x7D\x08\x01";
	//WriteProcessMemory(m_pHandle, (LPVOID)(0x02A6A563), newByte, sizeof(newByte) - 1, nullptr);
}
void CTitaniumApp::unsetDisableChatLangCheck()
{
	//char newByte[] = "\x83\x7D\x08\x00";
	//WriteProcessMemory(m_pHandle, (LPVOID)(0x02A6A563), newByte, sizeof(newByte) - 1, nullptr);
}



DWORD CTitaniumApp::FindPattern()
{
	//CString result = _T("Titanium Result");
	//return result;

	char *process = "MapleStory.exe";																													// The name of the process (and module) we need to access
	//char pattern[] = "\x68\x00\x00\x00\x00\xa1\x00\x00\x00\x00\x50\xe8\x00\x00\x00\x00\x83\xc4\x00\x8b\xc8\xff\x15\x00\x00\x00\x00\x3b\xdc";		// The pattern we are looking for
	//char mask[] = "x????x????xx????xx?xxxx????xx";																									// The mask we are using for that pattern

	char pattern[] = "\x55\x8B\xEC\x6A\xFF\x68\x00\x00\x00\x00\x64\xA1\x00\x00\x00\x00\x50\x81\xEC\x00\x00\x00\x00\xA1\x00\x00\x00\x00\x33\xC5\x89\x00\x00\x53\x56\x57\x50\x8D\x00\x00\x64\xA3\x00\x00\x00\x00\x89\x00\x00\xC7\x00\x00\x00\x00\x00\x00\x6A\x01\x8B\x4D\x00";		// The pattern we are looking for
	char mask[] = "xxxxxx????xxxxxxxxx????x????xxx??xxxxx??xx????x??x??????xxxx?";																									// The mask we are using for that pattern

	size_t origsize = strlen(process) + 1;
	const size_t newsize = 100;
	size_t convertedChars = 0;
	wchar_t procName[newsize];
	mbstowcs_s(&convertedChars, procName, origsize, process, _TRUNCATE);


	DWORD pID = GetPID(procName);																													// Obtain the process ID of the above process
	if (!pID) {																																		// If we can't get a valid process ID
		std::cout << "Could not find process" << std::endl;																							// Let the user know
		std::cin.get();																								// Return 1 and exit the program
	}

	HANDLE pHandle = OpenProcess(PROCESS_VM_OPERATION | PROCESS_VM_READ | PROCESS_VM_WRITE, false, pID);											// Obtain a handle to the process
	if (!pHandle) {																																	// If we can't get a handle to the process
		std::cout << "Could not obtain handle" << std::endl;																						// Let the user know
		std::cin.get();																																			// Return 1 and exit the program
	}

	DWORD address = ExternalAoBScan(pHandle, pID, procName, pattern, mask);																			// Run the AoB scan and store the returned address in the address varaible
	if (address) {																																	// If it's not NULL
		std::cout << "Address: " << std::hex << address << std::endl;																				// Print it to the screen
	}
	else {																																			// If it is null
		std::cout << "No address" << std::endl;																										// Let the user know
	}

	std::cin.get();																																	// Wait for the user to press enter
	return 0;
}
DWORD CTitaniumApp::GetPID(const wchar_t* procName)
{
	
	DWORD procId = 0;
	HANDLE hSnap = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
	if (hSnap != INVALID_HANDLE_VALUE)
	{
		PROCESSENTRY32 procEntry;
		procEntry.dwSize = sizeof(procEntry);
		if (Process32First(hSnap, &procEntry))
		{
			do
			{
				if (!_wcsicmp(procEntry.szExeFile, procName))
				{
					procId = procEntry.th32ProcessID;
					break;
				}
			} while (Process32Next(hSnap, &procEntry));
		}
	}
	CloseHandle(hSnap);
	return procId;
}
DWORD CTitaniumApp::GetModuleBaseAddress(DWORD pID, wchar_t *moduleName)
{
	MODULEENTRY32 entry;
	entry.dwSize = sizeof(MODULEENTRY32);
	DWORD baseAddress = 0;

	HANDLE snapshot = CreateToolhelp32Snapshot(TH32CS_SNAPMODULE, pID);					// Creates a snapshot of the process with the provided process ID

	if (Module32First(snapshot, &entry)) {												// Grabs the first modules information
		do {
			if (_wcsicmp(entry.szModule, moduleName) == 0) {							// Compares the module name to the argument passed into the function
				baseAddress = (DWORD)entry.modBaseAddr;									// if they are the same set the baseAddress variable to the base address of the module
				break;																	// and break out of the do while loop
			}
		} while (Module32Next(snapshot, &entry));										// continue scanning the next module in the snapshot
	}

	CloseHandle(snapshot);																// Close the handle since we're done with it
	return baseAddress;
}
DWORD CTitaniumApp::GetModuleSize(DWORD pID, wchar_t *moduleName)
{
	MODULEENTRY32 entry;
	entry.dwSize = sizeof(MODULEENTRY32);
	DWORD moduleSize = 0;

	HANDLE snapshot = CreateToolhelp32Snapshot(TH32CS_SNAPMODULE, pID);					// Creates a snapshot of the process with the provided process ID

	if (Module32First(snapshot, &entry)) {												// Grabs the first modules information
		do {
			if (_wcsicmp(entry.szModule, moduleName) == 0) {							// Compares the module name to the argument passed into the function
				moduleSize = (DWORD)entry.modBaseSize;									// if they are the same set the moduleSize variable to the size of the module
				break;																	// and break out of the do while loop
			}
		} while (Module32Next(snapshot, &entry));										// continue scanning the next module in the snapshot
	}

	CloseHandle(snapshot);																// Close the handle since we're done with it
	return moduleSize;
}
BOOL CTitaniumApp::ComparePattern(HANDLE pHandle, DWORD address, char *pattern, char *mask)
{
	DWORD patternSize = strlen(mask);													// Set the length of the pattern so we don't scan more than we need to

	auto memBuf = new char[patternSize + 1];											// Creaet a new char array with the length of the pattern size
	memset(memBuf, 0, patternSize + 1);													// Set the above array to all 0s
	ReadProcessMemory(pHandle, (LPVOID)address, memBuf, patternSize, 0);				// Read the memory from the address provied (With a langth of the pattern size to the above array


	for (DWORD i = 1; i < patternSize; i++) {											// For each byte in the above array

		if (memBuf[i] != pattern[i] && mask[i] != '?') {								// If the pattern at that index doesn't match the array at that index, and the mask doesn't have a wild card
			delete memBuf;																// Delete the buffer we created
			return false;																// Return false since the pattern didn't match
		}
	}
	delete memBuf;																		// Delete the buffer we created
	return true;
}
DWORD CTitaniumApp::ExternalAoBScan(HANDLE pHandle, DWORD pID, wchar_t *mod, char *pattern, char *mask)
{
	std::vector<DWORD> matches;																		// Create a vector to store all our results in
	DWORD patternSize = strlen(mask);																// Store the length of the pattern

	DWORD moduleBase = GetModuleBaseAddress(pID, mod);												// Get the base address of the module
	DWORD moduleSize = GetModuleSize(pID, mod);														// Get the size of the module

	if (!moduleBase || !moduleSize) {																// If either GetModuleBaseAddress or GetModuleSize returned NULL
		std::cout << "Could not get " << mod << " base address or size" << std::endl;				// Let the user know 
		return NULL;																				// Return NULL
	}

	auto moduleBytes = new char[moduleSize + 1];													// Create a new array of bytes the size of the module
	memset(moduleBytes, 0, moduleSize + 1);															// Set all the bytes in that array to 0
	ReadProcessMemory(pHandle, (LPVOID)moduleBase, moduleBytes, moduleSize, 0);						// Read the entire module into a local buffer that we can read from

	for (int i = 0; i + patternSize < moduleSize; i++) {											// For each byte in that module, if the index + the pattern size wont go past the end of the buffer
		if (pattern[0] == moduleBytes[i]) {															// If the first byte in the pattern is equal to the current byte in the module memory
			if (ComparePattern(pHandle, moduleBase + i, pattern, mask)) {							// Check if the entire pattern matches
				matches.push_back(moduleBase + i);													// If it does, push that address into the matches Vector
			}
		}
	}

	delete moduleBytes;																				// Delete the buffer we created

	if (matches.size() == 0) {																		// If there we no matches
		return NULL;																				// Return NULL
	}
	return matches[0];
}