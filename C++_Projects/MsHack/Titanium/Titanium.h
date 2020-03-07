// Titanium.h : main header file for the Titanium DLL
//

#pragma once

#ifndef __AFXWIN_H__
	#error "include 'pch.h' before including this file for PCH"
#endif

#include "resource.h"		// main symbols
#include <Windows.h>
#include <vector>
#include <TlHelp32.h>
#include <iostream>


#define ASM_RET                 0xC3
#define ASM_MOV1                0x8B 
#define ASM_MOV2                0x89
#define ASM_CALL                0xE8
#define ASM_JMP                 0xE9
#define ASM_CALL_SIZE           0x01
#define ASM_CALL_FULL_SIZE      0x05

#define JMP(frm,to) (((int)to - (int)frm)-5)

// CTitaniumApp
// See Titanium.cpp for the implementation of this class
//

class CTitaniumApp
{
public:

	DWORD m_pID;
	HANDLE m_pHandle;
	DWORD m_address;


	DWORD t_pID;
	HANDLE t_pHandle;
	DWORD t_address;

	DWORD H1_pID;
	HANDLE H1_pHandle;
	DWORD H1_address;

	DWORD H2_pID;
	HANDLE H2_pHandle;
	DWORD H2_address;

	DWORD H3_pID;
	HANDLE H3_pHandle;
	DWORD H3_address;

	__declspec(dllexport) CTitaniumApp();
	//__declspec(dllexport)  virtual ~CTitaniumApp();
	

	// CPU Hacks
	__declspec(dllexport) void setMSCRCBypass();
	__declspec(dllexport) void unsetMSCRCBypass();
	__declspec(dllexport) void setNoCatchBreath();
	__declspec(dllexport) void unsetNoCatchBreath();
	__declspec(dllexport) void setSkipVideoLogo();
	__declspec(dllexport) void unsetSkipVideoLogo();
	__declspec(dllexport) void setNoFadeInOut();
	__declspec(dllexport) void unsetNoFadeInOut();
	__declspec(dllexport) void setNoUITransparency();
	__declspec(dllexport) void unsetNoUITransparency();
	__declspec(dllexport) void setNoLootAnimation();
	__declspec(dllexport) void unsetNoLootAnimation();
	__declspec(dllexport) void setNoMobReaction();
	__declspec(dllexport) void unsetnoMobReaction();
	__declspec(dllexport) void setNoMultiKill();
	__declspec(dllexport) void unsetNoMultiKill();
	__declspec(dllexport) void setNoMobSpawn();
	__declspec(dllexport) void unsetNoMobSpawn();

	// Character Hacks
	__declspec(dllexport) void setUndetectedMissGodMode();
	__declspec(dllexport) void unsetUndetectedMissGodMode();
	__declspec(dllexport) void setBossGodMode();
	__declspec(dllexport) void unsetBossGodMode();
	__declspec(dllexport) void setSpecialBossDamage();
	__declspec(dllexport) void unsetSpecialBossDamage();
	__declspec(dllexport) void setAutoHPMP();
	__declspec(dllexport) void unsetAutoHPMP();
	__declspec(dllexport) void setUnlimitedFlash();
	__declspec(dllexport) void unsetUnlimitedFlash();
	__declspec(dllexport) void setGenericNoDelay();
	__declspec(dllexport) void unsetGenericNoDelay();
	__declspec(dllexport) void setUnlimitedAttack();
	__declspec(dllexport) void unsetUnlimitedAttack();
	__declspec(dllexport) void setPerfectStance();
	__declspec(dllexport) void unsetPerfectStance();
	__declspec(dllexport) void setNoDelayMining();
	__declspec(dllexport) void unsetNoDelayMining();
	__declspec(dllexport) void setInstantDrop();
	__declspec(dllexport) void unsetInstantDrop();
	__declspec(dllexport) void setInfiniteBuff();
	__declspec(dllexport) void unsetInfiniteBuff();
	__declspec(dllexport) void setConstantBlazing();
	__declspec(dllexport) void unsetConstantBlazing();
	__declspec(dllexport) void setFreezeBlazing();
	__declspec(dllexport) void unsetFreezeBlazing();
	__declspec(dllexport) void setNoBlazingExtinction();
	__declspec(dllexport) void unsetNoBlazingExtinction();


	// General Hacks
	__declspec(dllexport) void setAutoCC();
	__declspec(dllexport) void unsetAutoCC();
	__declspec(dllexport) void setPetLoot();
	__declspec(dllexport) void unsetPetLoot();
	__declspec(dllexport) void setInfiniteFamiliar();
	__declspec(dllexport) void unsetInfiniteFamiliar();
	__declspec(dllexport) void setDisableChatLangCheck();
	__declspec(dllexport) void unsetDisableChatLangCheck();

	
	bool Hook(void * toHook, void * ourFunct, int len);
	void PlaceJMP(BYTE *Address, DWORD jumpTo, DWORD length);


	DWORD FindPattern();
	DWORD GetPID(const wchar_t* procName);
	DWORD GetModuleBaseAddress(DWORD pID, wchar_t *moduleName);
	DWORD GetModuleSize(DWORD pID, wchar_t *moduleName);
	BOOL ComparePattern(HANDLE pHandle, DWORD address, char *pattern, char *mask);
	DWORD ExternalAoBScan(HANDLE pHandle, DWORD pID, wchar_t *mod, char *pattern, char *mask);

};
