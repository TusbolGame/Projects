#pragma once


// MobHackDlg dialog

class MobHackDlg : public CDialogEx
{
	DECLARE_DYNAMIC(MobHackDlg)

public:
	MobHackDlg(CWnd* pParent = nullptr);   // standard constructor
	virtual ~MobHackDlg();

// Dialog Data
#ifdef AFX_DESIGN_TIME
	enum { IDD = IDD_MobHackDlg };
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
};
