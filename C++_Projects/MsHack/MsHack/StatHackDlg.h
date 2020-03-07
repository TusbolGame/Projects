#pragma once


// StatHackDlg dialog

class StatHackDlg : public CDialogEx
{
	DECLARE_DYNAMIC(StatHackDlg)

public:
	StatHackDlg(CWnd* pParent = nullptr);   // standard constructor
	virtual ~StatHackDlg();

// Dialog Data
#ifdef AFX_DESIGN_TIME
	enum { IDD = IDD_StatHackDlg };
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnInitDialog();
	CSliderCtrl m_Slider1;
	CSliderCtrl m_Slider3;
	CSliderCtrl m_IEDSlider;
	CSliderCtrl m_DefenseSlider;
	CSliderCtrl m_JumpSlider;
	CSliderCtrl m_DamageSlider1;
	CSliderCtrl m_DamageSlider2;
	CSliderCtrl m_DamageSlider3;
	CSliderCtrl m_DamageSlider4;
	CSliderCtrl m_DamageSlider5;
	CSliderCtrl m_DamageSlider6;
	CSliderCtrl m_DamageSlider7;
	CSliderCtrl m_DamageSlider8;
	CSliderCtrl m_DamageSlider9;
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
};
