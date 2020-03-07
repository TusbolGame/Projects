
// MsHackDlg.cpp : implementation file
//

#include "pch.h"
#include "framework.h"
#include "MsHack.h"
#include "MsHackDlg.h"
#include "afxdialogex.h"


#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CMsHackDlg dialog



CMsHackDlg::CMsHackDlg(CWnd* pParent /*=nullptr*/)
	: CDialogEx(IDD_MSHACK_DIALOG, pParent)
	, m_pwndShow(NULL)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CMsHackDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_CATEGORYTAB, m_categoryTab);
}

BEGIN_MESSAGE_MAP(CMsHackDlg, CDialogEx)
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_NOTIFY(TCN_SELCHANGE, IDC_CATEGORYTAB, &CMsHackDlg::OnTcnSelchangeCategorytab)
	ON_NOTIFY(NM_CLICK, IDC_CATEGORYTAB, &CMsHackDlg::OnNMClickCategorytab)
	ON_WM_GETMINMAXINFO()
END_MESSAGE_MAP()


// CMsHackDlg message handlers


BOOL CMsHackDlg::OnInitDialog()
{
	CDialogEx::OnInitDialog();

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon

	// TODO: Add extra initialization here
	m_categoryTab.InsertItem(1, _T("CPU Hacks"));
	m_categoryTab.InsertItem(2, _T("Character Hacks"));
	m_categoryTab.InsertItem(3, _T("Character Stats Hacks"));
	m_categoryTab.InsertItem(4, _T("General Hacks"));
	m_categoryTab.InsertItem(5, _T("Mob Hacks"));


	CRect rc;
	GetWindowRect(&rc);
	GetWindowRect(&m_windowRect);

	CRect rect, rcWindow;
	m_categoryTab.GetClientRect(&rect);
	m_categoryTab.AdjustRect(FALSE, &rect);
	m_categoryTab.GetWindowRect(&rcWindow);
	ScreenToClient(rcWindow);
	rect.OffsetRect(rcWindow.left, rcWindow.top);

	m_mobhackDlg.Create(IDD_MobHackDlg, &m_categoryTab);
	m_mobhackDlg.SetWindowPos(NULL, 5, 25, rect.Width() - 5, rect.Height() -5, SWP_HIDEWINDOW | SWP_NOZORDER);

	m_GeneralHackDlg.Create(IDD_GeneralHackDlg, &m_categoryTab);
	m_GeneralHackDlg.SetWindowPos(NULL, 5, 25, rect.Width() - 5, rect.Height() - 5, SWP_HIDEWINDOW | SWP_NOZORDER);

	m_stathackDlg.Create(IDD_StatHackDlg, &m_categoryTab);
	m_stathackDlg.SetWindowPos(NULL, 5, 25, rect.Width() - 5, rect.Height() - 5, SWP_HIDEWINDOW | SWP_NOZORDER);
	
	m_characterhackDlg.Create(IDD_CharacterHackDlg, &m_categoryTab);
	m_characterhackDlg.SetWindowPos(NULL, 5, 25, rect.Width() - 5, rect.Height() - 5, SWP_HIDEWINDOW | SWP_NOZORDER);
	
	m_generalDlg.Create(IDD_GeneralDlg, &m_categoryTab);
	m_generalDlg.SetWindowPos(NULL, 5, 25, rect.Width() - 5, rect.Height() - 5, SWP_SHOWWINDOW | SWP_NOZORDER);
	m_pwndShow = &m_generalDlg;
	flag = true;

	return TRUE;  // return TRUE  unless you set the focus to a control
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CMsHackDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialogEx::OnPaint();
	}
}

// The system calls this function to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CMsHackDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}



void CMsHackDlg::OnTcnSelchangeCategorytab(NMHDR *pNMHDR, LRESULT *pResult)
{
	// TODO: Add your control notification handler code here
	if(m_pwndShow != NULL)
	{
		m_pwndShow->ShowWindow(SW_HIDE);
		m_pwndShow = NULL;
	}
	int mIndex = m_categoryTab.GetCurSel();
	switch (mIndex)
	{
		case 0:
			m_generalDlg.ShowWindow(SW_SHOW);
			m_pwndShow = &m_generalDlg;
			break;
		case 1:
			m_characterhackDlg.ShowWindow(SW_SHOW);
			m_pwndShow = &m_characterhackDlg;
			break;
		case 2:
			m_stathackDlg.ShowWindow(SW_SHOW);
			m_pwndShow = &m_stathackDlg;
			break;
		case 3:
			m_GeneralHackDlg.ShowWindow(SW_SHOW);
			m_pwndShow = &m_GeneralHackDlg;
			break;
		case 4:
			m_mobhackDlg.ShowWindow(SW_SHOW);
			m_pwndShow = &m_mobhackDlg;
			break;
		default:
			break;
		}
	*pResult = 0;
}


void CMsHackDlg::OnNMClickCategorytab(NMHDR *pNMHDR, LRESULT *pResult)
{
	// TODO: Add your control notification handler code here
	
	*pResult = 0;
}


void CMsHackDlg::OnGetMinMaxInfo(MINMAXINFO* lpMMI)
{
	// TODO: Add your message handler code here and/or call default
	if (flag)
	{
		((LPMINMAXINFO)lpMMI)->ptMinTrackSize.x = m_windowRect.Width();
		((LPMINMAXINFO)lpMMI)->ptMinTrackSize.y = m_windowRect.Height();
		((LPMINMAXINFO)lpMMI)->ptMaxTrackSize.x = m_windowRect.Width();
		((LPMINMAXINFO)lpMMI)->ptMaxTrackSize.y = m_windowRect.Height();
	}	
}
