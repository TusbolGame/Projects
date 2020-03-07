// CharacterHackDlg.cpp : implementation file
//

#include "pch.h"
#include "MsHack.h"
#include "CharacterHackDlg.h"
#include "afxdialogex.h"


// CharacterHackDlg dialog

IMPLEMENT_DYNAMIC(CharacterHackDlg, CDialogEx)

CharacterHackDlg::CharacterHackDlg(CWnd* pParent /*=nullptr*/)
	: CDialogEx(IDD_CharacterHackDlg, pParent)
{

}

CharacterHackDlg::~CharacterHackDlg()
{
}

void CharacterHackDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
}


BEGIN_MESSAGE_MAP(CharacterHackDlg, CDialogEx)
	ON_BN_CLICKED(IDC_AUTOHPMP_CHECK, &CharacterHackDlg::OnBnClickedAutohpmpCheck)
	ON_BN_CLICKED(IDC_MISSGODMODE_CHECK, &CharacterHackDlg::OnBnClickedMissgodmodeCheck)
	ON_BN_CLICKED(IDC_BOSSGODMODE_CHECK, &CharacterHackDlg::OnBnClickedBossgodmodeCheck)
	ON_BN_CLICKED(IDC_SPECIALBOSSDAMAGE_CHECK, &CharacterHackDlg::OnBnClickedSpecialbossdamageCheck)
	ON_BN_CLICKED(IDC_FLASHJUMP_CHECK, &CharacterHackDlg::OnBnClickedFlashjumpCheck)
	ON_BN_CLICKED(IDC_GENERICNODELAY_CHECK, &CharacterHackDlg::OnBnClickedGenericnodelayCheck)
	ON_BN_CLICKED(IDC_UNLIMITEDATTACK_CHECK, &CharacterHackDlg::OnBnClickedUnlimitedattackCheck)
	ON_BN_CLICKED(IDC_PERFECTSTANCE_CHECK, &CharacterHackDlg::OnBnClickedPerfectstanceCheck)
	ON_BN_CLICKED(IDC_NODELAYMINING_CHECK, &CharacterHackDlg::OnBnClickedNodelayminingCheck)
	ON_BN_CLICKED(IDC_INSTANTDROP_CHECK, &CharacterHackDlg::OnBnClickedInstantdropCheck)
	ON_BN_CLICKED(IDC_INFINITEBUFF_CHECK, &CharacterHackDlg::OnBnClickedInfinitebuffCheck)
	ON_BN_CLICKED(IDC_CONSTANTBLAZING_CHECK, &CharacterHackDlg::OnBnClickedConstantblazingCheck)
	ON_BN_CLICKED(IDC_FREEZEBLAZING_CHECK, &CharacterHackDlg::OnBnClickedFreezeblazingCheck)
	ON_BN_CLICKED(IDC_NOBLAZING_CHECK, &CharacterHackDlg::OnBnClickedNoblazingCheck)
END_MESSAGE_MAP()


// CharacterHackDlg message handlers

void CharacterHackDlg::OnBnClickedMissgodmodeCheck()
{
	// TODO: Add your control notification handler code here
	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_MISSGODMODE_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setUndetectedMissGodMode();
	}
	else
	{
		m_hack.unsetUndetectedMissGodMode();
	}
}


void CharacterHackDlg::OnBnClickedBossgodmodeCheck()
{
	// TODO: Add your control notification handler code here

	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_BOSSGODMODE_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setBossGodMode();
	}
	else
	{
		m_hack.unsetBossGodMode();
	}
}


void CharacterHackDlg::OnBnClickedSpecialbossdamageCheck()
{
	// TODO: Add your control notification handler code here

	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_SPECIALBOSSDAMAGE_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setSpecialBossDamage();
	}
	else
	{
		m_hack.unsetSpecialBossDamage();
	}
}


void CharacterHackDlg::OnBnClickedAutohpmpCheck()
{
	// TODO: Add your control notification handler code here
	CButton* checkBox;
	CEditView* hpEdit;
	CEditView* mpEdit;

	checkBox = (CButton *)GetDlgItem(IDC_AUTOHPMP_CHECK);
	hpEdit = (CEditView *)GetDlgItem(IDC_HP_EDIT);
	mpEdit = (CEditView *)GetDlgItem(IDC_MP_EDIT);

	int ChkBox = checkBox->GetCheck();
	char* hpStr;
	int _size = 100;
	hpStr = new char[_size];
	hpEdit->GetWindowTextW(LPTSTR(hpStr), _size);


	if (ChkBox == BST_CHECKED)
	{
		m_hack.setAutoHPMP();
	}
	else
	{
		m_hack.unsetAutoHPMP();
	}
}


void CharacterHackDlg::OnBnClickedFlashjumpCheck()
{
	// TODO: Add your control notification handler code here

	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_FLASHJUMP_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setUnlimitedFlash();
	}
	else
	{
		m_hack.unsetUnlimitedFlash();
	}
}


void CharacterHackDlg::OnBnClickedGenericnodelayCheck()
{
	// TODO: Add your control notification handler code here

	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_GENERICNODELAY_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setGenericNoDelay();
	}
	else
	{
		m_hack.unsetGenericNoDelay();
	}
}


void CharacterHackDlg::OnBnClickedUnlimitedattackCheck()
{
	// TODO: Add your control notification handler code here

	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_UNLIMITEDATTACK_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setUnlimitedAttack();
	}
	else
	{
		m_hack.unsetUnlimitedAttack();
	}
}


void CharacterHackDlg::OnBnClickedPerfectstanceCheck()
{
	// TODO: Add your control notification handler code here

	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_PERFECTSTANCE_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setPerfectStance();
	}
	else
	{
		m_hack.unsetPerfectStance();
	}
}


void CharacterHackDlg::OnBnClickedNodelayminingCheck()
{
	// TODO: Add your control notification handler code here

	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_NODELAYMINING_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setNoDelayMining();
	}
	else
	{
		m_hack.unsetNoDelayMining();
	}
}


void CharacterHackDlg::OnBnClickedInstantdropCheck()
{
	// TODO: Add your control notification handler code here
	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_INSTANTDROP_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setInstantDrop();
	}
	else
	{
		m_hack.unsetInstantDrop();
	}
}


void CharacterHackDlg::OnBnClickedInfinitebuffCheck()
{
	// TODO: Add your control notification handler code here

	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_INFINITEBUFF_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setInfiniteBuff();
	}
	else
	{
		m_hack.unsetInfiniteBuff();
	}
}


void CharacterHackDlg::OnBnClickedConstantblazingCheck()
{
	// TODO: Add your control notification handler code here

	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_CONSTANTBLAZING_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setConstantBlazing();
	}
	else
	{
		m_hack.unsetConstantBlazing();
	}
}


void CharacterHackDlg::OnBnClickedFreezeblazingCheck()
{
	// TODO: Add your control notification handler code here

	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_FREEZEBLAZING_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setFreezeBlazing();
	}
	else
	{
		m_hack.unsetFreezeBlazing();
	}
}


void CharacterHackDlg::OnBnClickedNoblazingCheck()
{
	// TODO: Add your control notification handler code here

	CButton* checkBox;

	checkBox = (CButton *)GetDlgItem(IDC_NOBLAZING_CHECK);

	int ChkBox = checkBox->GetCheck();

	if (ChkBox == BST_CHECKED)
	{
		m_hack.setNoBlazingExtinction();
	}
	else
	{
		m_hack.unsetNoBlazingExtinction();
	}
}
