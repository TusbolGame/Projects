// Gold.h : main header file for the Gold DLL
//

#pragma once

#ifndef __AFXWIN_H__
	#error "include 'pch.h' before including this file for PCH"
#endif

#include "resource.h"		// main symbols


// CGoldApp
// See Gold.cpp for the implementation of this class
//

class CGoldApp
{
public:
	__declspec(dllexport) CGoldApp();

	__declspec(dllexport) CString FindPattern();
};
