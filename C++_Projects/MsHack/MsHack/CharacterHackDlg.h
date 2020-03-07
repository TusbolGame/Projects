#pragma once

#include "../Titanium/Titanium.h"
// CharacterHackDlg dialog

class CharacterHackDlg : public CDialogEx
{
	DECLARE_DYNAMIC(CharacterHackDlg)

public:
	CharacterHackDlg(CWnd* pParent = nullptr);   // standard constructor
	virtual ~CharacterHackDlg();

// Dialog Data
#ifdef AFX_DESIGN_TIME
	enum { IDD = IDD_CharacterHackDlg };
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedAutohpmpCheck();
	CTitaniumApp m_hack;
	afx_msg void OnBnClickedMissgodmodeCheck();
	afx_msg void OnBnClickedBossgodmodeCheck();
	afx_msg void OnBnClickedSpecialbossdamageCheck();
	afx_msg void OnBnClickedFlashjumpCheck();
	afx_msg void OnBnClickedGenericnodelayCheck();
	afx_msg void OnBnClickedUnlimitedattackCheck();
	afx_msg void OnBnClickedPerfectstanceCheck();
	afx_msg void OnBnClickedNodelayminingCheck();
	afx_msg void OnBnClickedInstantdropCheck();
	afx_msg void OnBnClickedInfinitebuffCheck();
	afx_msg void OnBnClickedConstantblazingCheck();
	afx_msg void OnBnClickedFreezeblazingCheck();
	afx_msg void OnBnClickedNoblazingCheck();
};
