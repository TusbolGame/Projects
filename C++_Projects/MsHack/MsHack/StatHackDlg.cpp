// StatHackDlg.cpp : implementation file
//

#include "pch.h"
#include "MsHack.h"
#include "StatHackDlg.h"
#include "afxdialogex.h"


// StatHackDlg dialog

IMPLEMENT_DYNAMIC(StatHackDlg, CDialogEx)

StatHackDlg::StatHackDlg(CWnd* pParent /*=nullptr*/)
	: CDialogEx(IDD_StatHackDlg, pParent)
{

}

StatHackDlg::~StatHackDlg()
{
}

void StatHackDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_SLIDER1, m_Slider1);
	DDX_Control(pDX, IDC_SLIDER3, m_Slider3);
	DDX_Control(pDX, IDC_IED_SLIDER, m_IEDSlider);
	DDX_Control(pDX, IDC_DEFENSE_SLIDER, m_DefenseSlider);
	DDX_Control(pDX, IDC_JUMPSPEED_SLIDER, m_JumpSlider);
	DDX_Control(pDX, IDC_ANYDAMAGE_SLIDER1, m_DamageSlider1);
	DDX_Control(pDX, IDC_ANYDAMAGE_SLIDER2, m_DamageSlider2);
	DDX_Control(pDX, IDC_ANYDAMAGE_SLIDER3, m_DamageSlider3);
	DDX_Control(pDX, IDC_ANYDAMAGE_SLIDER4, m_DamageSlider4);
	DDX_Control(pDX, IDC_ANYDAMAGE_SLIDER5, m_DamageSlider5);
	DDX_Control(pDX, IDC_ANYDAMAGE_SLIDER6, m_DamageSlider6);
	DDX_Control(pDX, IDC_ANYDAMAGE_SLIDER7, m_DamageSlider7);
	DDX_Control(pDX, IDC_ANYDAMAGE_SLIDER8, m_DamageSlider8);
	DDX_Control(pDX, IDC_ANYDAMAGE_SLIDER9, m_DamageSlider9);
}


BEGIN_MESSAGE_MAP(StatHackDlg, CDialogEx)
	ON_WM_HSCROLL()
END_MESSAGE_MAP()


// StatHackDlg message handlers



BOOL StatHackDlg::OnInitDialog()
{
	CDialogEx::OnInitDialog();

	// TODO:  Add extra initialization here


	m_Slider1.SetRange(0, 100, TRUE);
	m_Slider1.SetPos(0);
	m_Slider3.SetRange(0, 100, TRUE);
	m_Slider3.SetPos(0);
	m_IEDSlider.SetRange(0, 100, TRUE);
	m_IEDSlider.SetPos(0);
	m_DefenseSlider.SetRange(0, 100, TRUE);
	m_DefenseSlider.SetPos(0);
	m_JumpSlider.SetRange(0, 100, TRUE);
	m_JumpSlider.SetPos(0);
	m_DamageSlider1.SetRange(0, 100, TRUE);
	m_DamageSlider1.SetPos(0);
	m_DamageSlider2.SetRange(0, 100, TRUE);
	m_DamageSlider2.SetPos(0);
	m_DamageSlider3.SetRange(0, 100, TRUE);
	m_DamageSlider3.SetPos(0);
	m_DamageSlider4.SetRange(0, 100, TRUE);
	m_DamageSlider4.SetPos(0);
	m_DamageSlider5.SetRange(0, 100, TRUE);
	m_DamageSlider5.SetPos(0);
	m_DamageSlider6.SetRange(0, 100, TRUE);
	m_DamageSlider6.SetPos(0);
	m_DamageSlider7.SetRange(0, 100, TRUE);
	m_DamageSlider7.SetPos(0);
	m_DamageSlider8.SetRange(0, 100, TRUE);
	m_DamageSlider8.SetPos(0);
	m_DamageSlider9.SetRange(0, 100, TRUE);
	m_DamageSlider9.SetPos(0);

	return TRUE;  // return TRUE unless you set the focus to a control
				  // EXCEPTION: OCX Property Pages should return FALSE
}


void StatHackDlg::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: Add your message handler code here and/or call default

	if (pScrollBar == (CScrollBar *)&m_Slider1) {
		int value = m_Slider1.GetPos();
		CString str;
		str.Format(_T("%d"), value);
		
		MessageBox(str);
	}
	else {
		//CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
	}
}
