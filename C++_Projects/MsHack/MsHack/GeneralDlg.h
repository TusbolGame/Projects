#pragma once
#include "../Gold/Gold.h"
#include "../Silver/Silver.h"
#include "../Titanium/Titanium.h"

// GeneralDlg dialog

class GeneralDlg : public CDialogEx
{
	DECLARE_DYNAMIC(GeneralDlg)

public:
	GeneralDlg(CWnd* pParent = nullptr);   // standard constructor
	CTitaniumApp m_hack;
	virtual ~GeneralDlg();

// Dialog Data
#ifdef AFX_DESIGN_TIME
	enum { IDD = IDD_GeneralDlg };
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedMscrcCheck();
	afx_msg void OnBnClickedNocatchbreathCheck();
	afx_msg void OnBnClickedSkipvideologoCheck();
	afx_msg void OnBnClickedNofadeCheck();
	afx_msg void OnBnClickedNouitrancparencyCheck();
	afx_msg void OnBnClickedNolootanimationCheck();
	afx_msg void OnBnClickedNomobreactionCheck();
	afx_msg void OnBnClickedNomultikillCheck();
	afx_msg void OnBnClickedNomobspawnCheck();
	
	virtual BOOL OnInitDialog();
};
