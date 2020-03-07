// GeneralDlg.cpp : implementation file
//

#include "pch.h"
#include "MsHack.h"
#include "GeneralDlg.h"
#include "afxdialogex.h"
#include "../Gold/Gold.h"
#include "../Silver/Silver.h"
#include "../Titanium/Titanium.h"


// GeneralDlg dialog

IMPLEMENT_DYNAMIC(GeneralDlg, CDialogEx)

GeneralDlg::GeneralDlg(CWnd* pParent /*=nullptr*/)
	: CDialogEx(IDD_GeneralDlg, pParent)
{

}

GeneralDlg::~GeneralDlg()
{
}

void GeneralDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
}


BEGIN_MESSAGE_MAP(GeneralDlg, CDialogEx)
	ON_BN_CLICKED(IDC_MSCRC_CHECK, &GeneralDlg::OnBnClickedMscrcCheck)
	ON_BN_CLICKED(IDC_NOCATCHBREATH_CHECK, &GeneralDlg::OnBnClickedNocatchbreathCheck)
	ON_BN_CLICKED(IDC_SKIPVIDEOLOGO_CHECK, &GeneralDlg::OnBnClickedSkipvideologoCheck)
	ON_BN_CLICKED(IDC_NOFADE_CHECK, &GeneralDlg::OnBnClickedNofadeCheck)
	ON_BN_CLICKED(IDC_NOUITRANCPARENCY_CHECK, &GeneralDlg::OnBnClickedNouitrancparencyCheck)
	ON_BN_CLICKED(IDC_NOLOOTANIMATION_CHECK, &GeneralDlg::OnBnClickedNolootanimationCheck)
	ON_BN_CLICKED(IDC_NOMOBREACTION_CHECK, &GeneralDlg::OnBnClickedNomobreactionCheck)
	ON_BN_CLICKED(IDC_NOMULTIKILL_CHECK, &GeneralDlg::OnBnClickedNomultikillCheck)
	ON_BN_CLICKED(IDC_NOMOBSPAWN_CHECK, &GeneralDlg::OnBnClickedNomobspawnCheck)
END_MESSAGE_MAP()


// GeneralDlg message handlers


void GeneralDlg::OnBnClickedMscrcCheck()
{
	// TODO: Add your control notification handler code here
	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_MSCRC_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setMSCRCBypass();
	}
	else
	{
		m_hack.unsetMSCRCBypass();
	}
}


void GeneralDlg::OnBnClickedNocatchbreathCheck()
{
	// TODO: Add your control notification handler code here
	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_NOCATCHBREATH_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setNoCatchBreath();
	}
	else
	{
		m_hack.unsetNoCatchBreath();
	}
}


void GeneralDlg::OnBnClickedSkipvideologoCheck()
{
	// TODO: Add your control notification handler code here
	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_SKIPVIDEOLOGO_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setSkipVideoLogo();
	}
	else
	{
		m_hack.unsetSkipVideoLogo();
	}
}


void GeneralDlg::OnBnClickedNofadeCheck()
{
	// TODO: Add your control notification handler code here
	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_NOFADE_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setNoFadeInOut();
	}
	else
	{
		m_hack.unsetNoFadeInOut();
	}
}


void GeneralDlg::OnBnClickedNouitrancparencyCheck()
{
	// TODO: Add your control notification handler code here
	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_NOUITRANCPARENCY_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setNoUITransparency();
	}
	else
	{
		m_hack.unsetNoUITransparency();
	}
}


void GeneralDlg::OnBnClickedNolootanimationCheck()
{
	// TODO: Add your control notification handler code here
	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_NOLOOTANIMATION_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setNoLootAnimation();
	}
	else
	{
		m_hack.unsetNoLootAnimation();
	}
}


void GeneralDlg::OnBnClickedNomobreactionCheck()
{
	// TODO: Add your control notification handler code here
	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_NOMOBREACTION_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setNoMobReaction();
	}
	else
	{
		m_hack.unsetnoMobReaction();
	}
}


void GeneralDlg::OnBnClickedNomultikillCheck()
{
	// TODO: Add your control notification handler code here
	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_NOMULTIKILL_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setNoMultiKill();
	}
	else
	{
		m_hack.unsetNoMultiKill();
	}
}


void GeneralDlg::OnBnClickedNomobspawnCheck()
{
	// TODO: Add your control notification handler code here
	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_NOMOBSPAWN_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setNoMobSpawn();
	}
	else
	{
		m_hack.unsetNoMobSpawn();
	}
}


BOOL GeneralDlg::OnInitDialog()
{
	CDialogEx::OnInitDialog();

	// TODO:  Add extra initialization here

	return TRUE;  // return TRUE unless you set the focus to a control
				  // EXCEPTION: OCX Property Pages should return FALSE
}
