
// MsHackDlg.h : header file
//
#include "GeneralDlg.h"
#include "GeneralHackDlg.h"
#include "CharacterHackDlg.h"
#include "MobHackDlg.h"
#include "StatHackDlg.h"
#include "../Gold/Gold.h"
#include "../Silver/Silver.h"
#include "../Titanium/Titanium.h"
#pragma once


// CMsHackDlg dialog
class CMsHackDlg : public CDialogEx
{
// Construction
public:
	CMsHackDlg(CWnd* pParent = nullptr);	// standard constructor

// Dialog Data
#ifdef AFX_DESIGN_TIME
	enum { IDD = IDD_MSHACK_DIALOG };
#endif

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support


// Implementation
protected:
	HICON m_hIcon;

	// Generated message map functions
	virtual BOOL OnInitDialog();
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnTcnSelchangeCategorytab(NMHDR *pNMHDR, LRESULT *pResult);
	CTabCtrl m_categoryTab;
	
	CRect m_windowRect;
	bool flag = false;
	
	GeneralHackDlg m_GeneralHackDlg;
	GeneralDlg m_generalDlg;
	MobHackDlg m_mobhackDlg;
	StatHackDlg m_stathackDlg;
	CharacterHackDlg m_characterhackDlg;
	CWnd* m_pwndShow;
	afx_msg void OnNMClickCategorytab(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnGetMinMaxInfo(MINMAXINFO* lpMMI);
};
