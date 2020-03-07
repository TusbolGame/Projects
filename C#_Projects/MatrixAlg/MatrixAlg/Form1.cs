using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;
using WcfService;
using System.ServiceModel;

namespace MatrixAlg
{
    public partial class Form1 : Form
    {
        
        public List<double> _max;

        private BackgroundWorker _bgwork;

        private string Contract_matrix_data;
        private string A_matrix_data;
        private string B_matrix_data;
        private string C_matrix_data;
        private string D_matrix_data;
        private string P_matrix_data;
        private string Binary_matrix_data;

        private int product, player;

        public Form1()
        {
            InitializeComponent();
            _max = new List<double>();
        }

        private ChannelFactory<IService1> remoteFactory;
        private IService1[] remoteProxy;
        private MyAlgorithm _alg;
        private bool isConnected = false;

        private void btnGenerate_Click(object sender, EventArgs e)
        {
            
            if(!Int32.TryParse(txtProduct.Text, out product))
            {
                MessageBox.Show("Please input & correct the Product");
                return;
            }

            if (!Int32.TryParse(txtPlayer.Text, out player))
            {
                MessageBox.Show("Please input & correct the Player");
                return;
            }

            remoteFactory = new ChannelFactory<IService1>("ServiceConfig");
            _alg = new MyAlgorithm(product, player, product);

            UpdateStatus(0, "Created");
            UpdateStatus(1, "Created");
            UpdateStatus(2, "Created");
            UpdateStatus(3, "Created");
            UpdateStatus(4, "Created");
            UpdateStatus(5, "Created");
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            lblStatusContract.Text = "";
            lblStatusB.Text = "";
            lblStatusA.Text = "";
            lblStatusBinary.Text = "";
            _alg = new MyAlgorithm();
        }

        private void btnContentNew_Click(object sender, EventArgs e)
        {
            txtContent.Text = "";
        }

        private void btnContentOpen_Click(object sender, EventArgs e)
        {
            // open file dialog
            OpenFileDialog ofd = new OpenFileDialog();
            ofd.Filter = "Text Files (*.txt)|*.txt";
            ofd.Title = "Select file for assignment";

            if(ofd.ShowDialog() == DialogResult.OK)
            {
                string lines = File.ReadAllText(ofd.FileName);
                txtContent.Text = lines;
            }
        }

        private void btnContentSave_Click(object sender, EventArgs e)
        {
            // save file dialog
            SaveFileDialog sfd = new SaveFileDialog();
            sfd.Filter = "Text Files (*.txt)|*.txt";
            sfd.Title = "Save assignment file";
            if(sfd.ShowDialog() == DialogResult.OK)
            {
                string lines = txtContent.Text;
                StreamWriter sw = new StreamWriter(sfd.FileName);
                sw.Write(lines);
                sw.Close();
                MessageBox.Show("Saved");
            }
        }

        private void btnAssign_Click(object sender, EventArgs e)
        {
            if(cmbType.SelectedIndex == -1)
            {
                MessageBox.Show("Please select the type");
                return;
            }
            string lines = txtContent.Text;
            string res = _alg.Assign(lines, cmbType.SelectedIndex);

            if (res == "Success")
            {   
                UpdateStatus(cmbType.SelectedIndex, "Assigned");
                if (cmbType.SelectedIndex == 0)
                {
                    Contract_matrix_data = lines;
                }
                else if (cmbType.SelectedIndex == 1)
                {
                    A_matrix_data = lines;
                }
                else if (cmbType.SelectedIndex == 2)
                {
                    B_matrix_data = lines;
                }
                else if (cmbType.SelectedIndex == 3)
                {
                    C_matrix_data = lines;
                }
                else if (cmbType.SelectedIndex == 4)
                {
                    D_matrix_data = lines;
                }
                else if (cmbType.SelectedIndex == 5)
                {
                    P_matrix_data = lines;
                }
            }
        }

        private void cmbType_SelectedIndexChanged(object sender, EventArgs e)
        {
            if(cmbType.SelectedIndex == -1)
            {
                return;
            }
            bool[] b = { _alg._assigned_contract, _alg._assigned_A, _alg._assigned_B, _alg._assigned_C, _alg._assigned_D, _alg._assigned_P};
            if(b[cmbType.SelectedIndex])
            {
                // if there is assigned data then display in text
                txtContent.Text = _alg.getAssignedData(cmbType.SelectedIndex);
            } else
            {
                txtContent.Text = "";
            }
        }

        private void btnAssignClear_Click(object sender, EventArgs e)
        {
            if (cmbType.SelectedIndex == -1)
            {
                MessageBox.Show("Please select the type to remove the assignment");
                return;
            }
            _alg.Reset(cmbType.SelectedIndex);
            UpdateStatus(cmbType.SelectedIndex, "Created");
        }

        public void UpdateStatus(int type, string status)
        {
            if(type == 0)
            {
                lblStatusContract.Text = "Contract ( " + _alg._np.ToString() + " x " + _alg._m.ToString() + " ) : " + status;
            }
            else if(type == 1)
            {
                lblStatusA.Text = "A ( " + _alg._np.ToString() + " x " + _alg._necessity.ToString() + " ) : " + status;
            }
            else if(type == 2)
            {
                lblStatusB.Text = "B ( " + _alg._np.ToString() + " x " + _alg._m.ToString() + " ) : " + status;
            }
            else if(type == 3)
            {
                lblStatusC.Text = "C ( " + _alg._np.ToString() + " x 1 ) : " + status;
            }
            else if(type == 4)
            {
                lblStatusD.Text = "D ( 2 x " + _alg._np.ToString() + " ) : " + status;
            }
            else if(type == 5)
            {
                lblStatusP.Text = "P ( " + _alg._np.ToString() + " x 1 ) : " + status;
            }
            else if(type == 6)
            {
                lblStatusBinary.Text = "Binary ( " + _alg._br.ToString() + " x " + _alg._m.ToString() + " ) : " + status;
            }
        }

        private void btnCalc_Click(object sender, EventArgs e)
        {
            if(!_alg._assigned_contract || !_alg._assigned_A || !_alg._assigned_B || !_alg._assigned_C || !_alg._assigned_D || !_alg._assigned_P )
            {
                MessageBox.Show("All data should be assigned. Please assign data first.");
                return;
            }

            remoteProxy = new IService1[_alg._br];            

            for (int k = 0; k < _alg._br; k++)
            {
                remoteProxy[k] = remoteFactory.CreateChannel();
                //string result = remoteProxy[k].SetCalcInstance(_alg);
                string result = remoteProxy[k].CreateCalcInstance(product, player, product);
                remoteProxy[k].AssignMatrix(Contract_matrix_data, 0);
                remoteProxy[k].AssignMatrix(A_matrix_data, 1);
                remoteProxy[k].AssignMatrix(B_matrix_data, 2);
                remoteProxy[k].AssignMatrix(C_matrix_data, 3);
                remoteProxy[k].AssignMatrix(D_matrix_data, 4);
                remoteProxy[k].AssignMatrix(P_matrix_data, 5);

                if (result == "success")
                {
                    remoteProxy[k].CalculateResult(k);
                    //MessageBox.Show(remoteProxy[k].GetRestrictionString());
                }
                else
                    return;
            }
            Service_Timer.Enabled = true;
        }

        private void Service_Timer_Tick(object sender, EventArgs e)
        {

            for (int k = 0; k < _alg._br; k++)
            {
                if (remoteProxy[k].GetStatus() == 1)
                {
                    lstResult.Items.Add(k.ToString() + "**Restrict : " + remoteProxy[k].GetRestrictionString());
                }
                else if (remoteProxy[k].GetStatus() == 2)
                {
                    lstResult.Items.Add(k.ToString() + "**Max Equ : " + remoteProxy[k].GetMaxString());
                }
                else if (remoteProxy[k].GetStatus() == 3)
                {
                    lstResult.Items.Add(k.ToString() + "**Max Value: " + remoteProxy[k].GetMaxValue());
                }
                else if (remoteProxy[k].GetStatus() == 4)
                {
                    lstResult.Items.Add(k.ToString() + "**Max : " + remoteProxy[k].GetResult().ToString());
                }

            }
        }
    }
}
