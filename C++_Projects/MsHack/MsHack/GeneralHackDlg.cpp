// GeneralHackDlg.cpp : implementation file
//

#include "pch.h"
#include "MsHack.h"
#include "GeneralHackDlg.h"
#include "afxdialogex.h"


// GeneralHackDlg dialog

IMPLEMENT_DYNAMIC(GeneralHackDlg, CDialogEx)

GeneralHackDlg::GeneralHackDlg(CWnd* pParent /*=nullptr*/)
	: CDialogEx(IDD_GeneralHackDlg, pParent)
{

}

GeneralHackDlg::~GeneralHackDlg()
{
}

void GeneralHackDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
}


BEGIN_MESSAGE_MAP(GeneralHackDlg, CDialogEx)
	ON_BN_CLICKED(IDC_AUTOCC_CHECK, &GeneralHackDlg::OnBnClickedAutoccCheck)
	ON_BN_CLICKED(IDC_PETLOOT_CHECK, &GeneralHackDlg::OnBnClickedPetlootCheck)
	ON_BN_CLICKED(IDC_INFINITEFAMILIAR_CHECK, &GeneralHackDlg::OnBnClickedInfinitefamiliarCheck)
	ON_BN_CLICKED(IDC_DISABLECHATLANG_CHECK, &GeneralHackDlg::OnBnClickedDisablechatlangCheck)
END_MESSAGE_MAP()


// GeneralHackDlg message handlers


void GeneralHackDlg::OnBnClickedAutoccCheck()
{
	// TODO: Add your control notification handler code here

	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_AUTOCC_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setAutoCC();
	}
	else
	{
		m_hack.unsetAutoCC();
	}
}


void GeneralHackDlg::OnBnClickedPetlootCheck()
{
	// TODO: Add your control notification handler code here

	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_PETLOOT_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setPetLoot();
	}
	else
	{
		m_hack.unsetPetLoot();
	}
}


void GeneralHackDlg::OnBnClickedInfinitefamiliarCheck()
{
	// TODO: Add your control notification handler code here

	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_INFINITEFAMILIAR_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setInfiniteFamiliar();
	}
	else
	{
		m_hack.unsetInfiniteFamiliar();
	}
}


void GeneralHackDlg::OnBnClickedDisablechatlangCheck()
{
	// TODO: Add your control notification handler code here

	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_DISABLECHATLANG_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setDisableChatLangCheck();
	}
	else
	{
		m_hack.unsetDisableChatLangCheck();
	}
}
