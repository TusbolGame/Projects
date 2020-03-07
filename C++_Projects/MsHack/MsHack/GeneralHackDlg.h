#pragma once

#include "../Titanium/Titanium.h"
// GeneralHackDlg dialog

class GeneralHackDlg : public CDialogEx
{
	DECLARE_DYNAMIC(GeneralHackDlg)

public:
	GeneralHackDlg(CWnd* pParent = nullptr);   // standard constructor
	virtual ~GeneralHackDlg();

// Dialog Data
#ifdef AFX_DESIGN_TIME
	enum { IDD = IDD_GeneralHackDlg };
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedAutoccCheck();
	afx_msg void OnBnClickedPetlootCheck();
	afx_msg void OnBnClickedInfinitefamiliarCheck();
	afx_msg void OnBnClickedDisablechatlangCheck();
	CTitaniumApp m_hack;
};
