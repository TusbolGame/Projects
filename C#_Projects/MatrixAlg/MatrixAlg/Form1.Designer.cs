namespace MatrixAlg
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.btnGenerate = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.txtPlayer = new System.Windows.Forms.TextBox();
            this.lblProduct = new System.Windows.Forms.Label();
            this.txtProduct = new System.Windows.Forms.TextBox();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.lblStatusP = new System.Windows.Forms.Label();
            this.lblStatusD = new System.Windows.Forms.Label();
            this.lblStatusC = new System.Windows.Forms.Label();
            this.lblStatus = new System.Windows.Forms.Label();
            this.lblStatusBinary = new System.Windows.Forms.Label();
            this.lblStatusA = new System.Windows.Forms.Label();
            this.lblStatusB = new System.Windows.Forms.Label();
            this.lblStatusContract = new System.Windows.Forms.Label();
            this.groupBox3 = new System.Windows.Forms.GroupBox();
            this.groupBox5 = new System.Windows.Forms.GroupBox();
            this.lstResult = new System.Windows.Forms.ListBox();
            this.txtContent = new System.Windows.Forms.TextBox();
            this.groupBox4 = new System.Windows.Forms.GroupBox();
            this.btnCalc = new System.Windows.Forms.Button();
            this.btnAssign = new System.Windows.Forms.Button();
            this.cmbType = new System.Windows.Forms.ComboBox();
            this.btnAssignClear = new System.Windows.Forms.Button();
            this.btnContentOpen = new System.Windows.Forms.Button();
            this.btnContentSave = new System.Windows.Forms.Button();
            this.btnContentNew = new System.Windows.Forms.Button();
            this.Service_Timer = new System.Windows.Forms.Timer(this.components);
            this.groupBox1.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.groupBox3.SuspendLayout();
            this.groupBox5.SuspendLayout();
            this.groupBox4.SuspendLayout();
            this.SuspendLayout();
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.btnGenerate);
            this.groupBox1.Controls.Add(this.label1);
            this.groupBox1.Controls.Add(this.txtPlayer);
            this.groupBox1.Controls.Add(this.lblProduct);
            this.groupBox1.Controls.Add(this.txtProduct);
            this.groupBox1.Dock = System.Windows.Forms.DockStyle.Top;
            this.groupBox1.Location = new System.Drawing.Point(0, 0);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(1287, 59);
            this.groupBox1.TabIndex = 1;
            this.groupBox1.TabStop = false;
            // 
            // btnGenerate
            // 
            this.btnGenerate.Location = new System.Drawing.Point(550, 24);
            this.btnGenerate.Name = "btnGenerate";
            this.btnGenerate.Size = new System.Drawing.Size(75, 23);
            this.btnGenerate.TabIndex = 13;
            this.btnGenerate.Text = "Generate";
            this.btnGenerate.UseVisualStyleBackColor = true;
            this.btnGenerate.Click += new System.EventHandler(this.btnGenerate_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(305, 28);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(41, 13);
            this.label1.TabIndex = 10;
            this.label1.Text = "Players";
            // 
            // txtPlayer
            // 
            this.txtPlayer.Location = new System.Drawing.Point(355, 25);
            this.txtPlayer.Name = "txtPlayer";
            this.txtPlayer.Size = new System.Drawing.Size(88, 20);
            this.txtPlayer.TabIndex = 9;
            this.txtPlayer.Text = "2";
            // 
            // lblProduct
            // 
            this.lblProduct.AutoSize = true;
            this.lblProduct.Location = new System.Drawing.Point(136, 28);
            this.lblProduct.Name = "lblProduct";
            this.lblProduct.Size = new System.Drawing.Size(44, 13);
            this.lblProduct.TabIndex = 8;
            this.lblProduct.Text = "Product";
            // 
            // txtProduct
            // 
            this.txtProduct.Location = new System.Drawing.Point(186, 25);
            this.txtProduct.Name = "txtProduct";
            this.txtProduct.Size = new System.Drawing.Size(83, 20);
            this.txtProduct.TabIndex = 7;
            this.txtProduct.Text = "2";
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.lblStatusP);
            this.groupBox2.Controls.Add(this.lblStatusD);
            this.groupBox2.Controls.Add(this.lblStatusC);
            this.groupBox2.Controls.Add(this.lblStatus);
            this.groupBox2.Controls.Add(this.lblStatusBinary);
            this.groupBox2.Controls.Add(this.lblStatusA);
            this.groupBox2.Controls.Add(this.lblStatusB);
            this.groupBox2.Controls.Add(this.lblStatusContract);
            this.groupBox2.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.groupBox2.Location = new System.Drawing.Point(0, 561);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(1287, 79);
            this.groupBox2.TabIndex = 2;
            this.groupBox2.TabStop = false;
            // 
            // lblStatusP
            // 
            this.lblStatusP.AutoSize = true;
            this.lblStatusP.Location = new System.Drawing.Point(37, 47);
            this.lblStatusP.Name = "lblStatusP";
            this.lblStatusP.Size = new System.Drawing.Size(118, 13);
            this.lblStatusP.TabIndex = 7;
            this.lblStatusP.Text = "P ( 100 x 100) : created";
            // 
            // lblStatusD
            // 
            this.lblStatusD.AutoSize = true;
            this.lblStatusD.Location = new System.Drawing.Point(393, 47);
            this.lblStatusD.Name = "lblStatusD";
            this.lblStatusD.Size = new System.Drawing.Size(119, 13);
            this.lblStatusD.TabIndex = 6;
            this.lblStatusD.Text = "D ( 100 x 100) : created";
            // 
            // lblStatusC
            // 
            this.lblStatusC.AutoSize = true;
            this.lblStatusC.Location = new System.Drawing.Point(206, 47);
            this.lblStatusC.Name = "lblStatusC";
            this.lblStatusC.Size = new System.Drawing.Size(118, 13);
            this.lblStatusC.TabIndex = 5;
            this.lblStatusC.Text = "C ( 100 x 100) : created";
            // 
            // lblStatus
            // 
            this.lblStatus.AutoSize = true;
            this.lblStatus.Location = new System.Drawing.Point(604, 47);
            this.lblStatus.Name = "lblStatus";
            this.lblStatus.Size = new System.Drawing.Size(37, 13);
            this.lblStatus.TabIndex = 4;
            this.lblStatus.Text = "Status";
            // 
            // lblStatusBinary
            // 
            this.lblStatusBinary.AutoSize = true;
            this.lblStatusBinary.Location = new System.Drawing.Point(594, 20);
            this.lblStatusBinary.Name = "lblStatusBinary";
            this.lblStatusBinary.Size = new System.Drawing.Size(140, 13);
            this.lblStatusBinary.TabIndex = 3;
            this.lblStatusBinary.Text = "Binary ( 100 x 100) : created";
            // 
            // lblStatusA
            // 
            this.lblStatusA.AutoSize = true;
            this.lblStatusA.Location = new System.Drawing.Point(233, 19);
            this.lblStatusA.Name = "lblStatusA";
            this.lblStatusA.Size = new System.Drawing.Size(123, 13);
            this.lblStatusA.TabIndex = 2;
            this.lblStatusA.Text = "Ari ( 100 x 100) : created";
            // 
            // lblStatusB
            // 
            this.lblStatusB.AutoSize = true;
            this.lblStatusB.Location = new System.Drawing.Point(430, 20);
            this.lblStatusB.Name = "lblStatusB";
            this.lblStatusB.Size = new System.Drawing.Size(118, 13);
            this.lblStatusB.TabIndex = 1;
            this.lblStatusB.Text = "B ( 100 x 100) : created";
            // 
            // lblStatusContract
            // 
            this.lblStatusContract.AutoSize = true;
            this.lblStatusContract.Location = new System.Drawing.Point(11, 19);
            this.lblStatusContract.Name = "lblStatusContract";
            this.lblStatusContract.Size = new System.Drawing.Size(151, 13);
            this.lblStatusContract.TabIndex = 0;
            this.lblStatusContract.Text = "Contract ( 100 x 100) : created";
            // 
            // groupBox3
            // 
            this.groupBox3.Controls.Add(this.groupBox5);
            this.groupBox3.Controls.Add(this.txtContent);
            this.groupBox3.Controls.Add(this.groupBox4);
            this.groupBox3.Dock = System.Windows.Forms.DockStyle.Fill;
            this.groupBox3.Location = new System.Drawing.Point(0, 59);
            this.groupBox3.Margin = new System.Windows.Forms.Padding(0);
            this.groupBox3.Name = "groupBox3";
            this.groupBox3.Padding = new System.Windows.Forms.Padding(0);
            this.groupBox3.Size = new System.Drawing.Size(1287, 502);
            this.groupBox3.TabIndex = 3;
            this.groupBox3.TabStop = false;
            // 
            // groupBox5
            // 
            this.groupBox5.Controls.Add(this.lstResult);
            this.groupBox5.Dock = System.Windows.Forms.DockStyle.Fill;
            this.groupBox5.Location = new System.Drawing.Point(480, 63);
            this.groupBox5.Name = "groupBox5";
            this.groupBox5.Size = new System.Drawing.Size(807, 439);
            this.groupBox5.TabIndex = 4;
            this.groupBox5.TabStop = false;
            this.groupBox5.Text = "Result";
            // 
            // lstResult
            // 
            this.lstResult.Dock = System.Windows.Forms.DockStyle.Fill;
            this.lstResult.Font = new System.Drawing.Font("Microsoft Sans Serif", 11F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lstResult.FormattingEnabled = true;
            this.lstResult.ItemHeight = 18;
            this.lstResult.Location = new System.Drawing.Point(3, 16);
            this.lstResult.Name = "lstResult";
            this.lstResult.Size = new System.Drawing.Size(801, 420);
            this.lstResult.TabIndex = 0;
            // 
            // txtContent
            // 
            this.txtContent.Dock = System.Windows.Forms.DockStyle.Left;
            this.txtContent.Font = new System.Drawing.Font("Microsoft Sans Serif", 11F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.txtContent.Location = new System.Drawing.Point(0, 63);
            this.txtContent.Multiline = true;
            this.txtContent.Name = "txtContent";
            this.txtContent.Size = new System.Drawing.Size(480, 439);
            this.txtContent.TabIndex = 3;
            // 
            // groupBox4
            // 
            this.groupBox4.Controls.Add(this.btnCalc);
            this.groupBox4.Controls.Add(this.btnAssign);
            this.groupBox4.Controls.Add(this.cmbType);
            this.groupBox4.Controls.Add(this.btnAssignClear);
            this.groupBox4.Controls.Add(this.btnContentOpen);
            this.groupBox4.Controls.Add(this.btnContentSave);
            this.groupBox4.Controls.Add(this.btnContentNew);
            this.groupBox4.Dock = System.Windows.Forms.DockStyle.Top;
            this.groupBox4.Location = new System.Drawing.Point(0, 13);
            this.groupBox4.Margin = new System.Windows.Forms.Padding(0);
            this.groupBox4.Name = "groupBox4";
            this.groupBox4.Padding = new System.Windows.Forms.Padding(0);
            this.groupBox4.Size = new System.Drawing.Size(1287, 50);
            this.groupBox4.TabIndex = 1;
            this.groupBox4.TabStop = false;
            // 
            // btnCalc
            // 
            this.btnCalc.Location = new System.Drawing.Point(765, 25);
            this.btnCalc.Name = "btnCalc";
            this.btnCalc.Size = new System.Drawing.Size(60, 25);
            this.btnCalc.TabIndex = 8;
            this.btnCalc.Text = "Calculate";
            this.btnCalc.UseVisualStyleBackColor = true;
            this.btnCalc.Click += new System.EventHandler(this.btnCalc_Click);
            // 
            // btnAssign
            // 
            this.btnAssign.Location = new System.Drawing.Point(369, 19);
            this.btnAssign.Name = "btnAssign";
            this.btnAssign.Size = new System.Drawing.Size(59, 25);
            this.btnAssign.TabIndex = 7;
            this.btnAssign.Text = "Assign";
            this.btnAssign.UseVisualStyleBackColor = true;
            this.btnAssign.Click += new System.EventHandler(this.btnAssign_Click);
            // 
            // cmbType
            // 
            this.cmbType.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cmbType.FormattingEnabled = true;
            this.cmbType.Items.AddRange(new object[] {
            "Contract Matrix",
            "Ari (resource matrix A)",
            "RAM (resource matrix B)",
            "Cost Vector (C)",
            "Distribute Matrix (μ, σ) (D)",
            "Sales Vector (P)"});
            this.cmbType.Location = new System.Drawing.Point(216, 22);
            this.cmbType.Name = "cmbType";
            this.cmbType.Size = new System.Drawing.Size(140, 21);
            this.cmbType.TabIndex = 6;
            this.cmbType.SelectedIndexChanged += new System.EventHandler(this.cmbType_SelectedIndexChanged);
            // 
            // btnAssignClear
            // 
            this.btnAssignClear.Location = new System.Drawing.Point(441, 19);
            this.btnAssignClear.Name = "btnAssignClear";
            this.btnAssignClear.Size = new System.Drawing.Size(56, 25);
            this.btnAssignClear.TabIndex = 4;
            this.btnAssignClear.Text = "Remove";
            this.btnAssignClear.UseVisualStyleBackColor = true;
            this.btnAssignClear.Click += new System.EventHandler(this.btnAssignClear_Click);
            // 
            // btnContentOpen
            // 
            this.btnContentOpen.Location = new System.Drawing.Point(61, 19);
            this.btnContentOpen.Name = "btnContentOpen";
            this.btnContentOpen.Size = new System.Drawing.Size(43, 25);
            this.btnContentOpen.TabIndex = 3;
            this.btnContentOpen.Text = "Open";
            this.btnContentOpen.UseVisualStyleBackColor = true;
            this.btnContentOpen.Click += new System.EventHandler(this.btnContentOpen_Click);
            // 
            // btnContentSave
            // 
            this.btnContentSave.Location = new System.Drawing.Point(112, 21);
            this.btnContentSave.Name = "btnContentSave";
            this.btnContentSave.Size = new System.Drawing.Size(43, 25);
            this.btnContentSave.TabIndex = 2;
            this.btnContentSave.Text = "Save";
            this.btnContentSave.UseVisualStyleBackColor = true;
            this.btnContentSave.Click += new System.EventHandler(this.btnContentSave_Click);
            // 
            // btnContentNew
            // 
            this.btnContentNew.Location = new System.Drawing.Point(9, 19);
            this.btnContentNew.Name = "btnContentNew";
            this.btnContentNew.Size = new System.Drawing.Size(43, 25);
            this.btnContentNew.TabIndex = 1;
            this.btnContentNew.Text = "New";
            this.btnContentNew.UseVisualStyleBackColor = true;
            this.btnContentNew.Click += new System.EventHandler(this.btnContentNew_Click);
            // 
            // Service_Timer
            // 
            this.Service_Timer.Interval = 1000;
            this.Service_Timer.Tick += new System.EventHandler(this.Service_Timer_Tick);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1287, 640);
            this.Controls.Add(this.groupBox3);
            this.Controls.Add(this.groupBox2);
            this.Controls.Add(this.groupBox1);
            this.Name = "Form1";
            this.Text = "Form1";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            this.groupBox3.ResumeLayout(false);
            this.groupBox3.PerformLayout();
            this.groupBox5.ResumeLayout(false);
            this.groupBox4.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Button btnGenerate;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox txtPlayer;
        private System.Windows.Forms.Label lblProduct;
        private System.Windows.Forms.TextBox txtProduct;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.Label lblStatusBinary;
        private System.Windows.Forms.Label lblStatusA;
        private System.Windows.Forms.Label lblStatusB;
        private System.Windows.Forms.Label lblStatusContract;
        private System.Windows.Forms.GroupBox groupBox3;
        private System.Windows.Forms.TextBox txtContent;
        private System.Windows.Forms.GroupBox groupBox4;
        private System.Windows.Forms.Button btnCalc;
        private System.Windows.Forms.Button btnAssign;
        private System.Windows.Forms.ComboBox cmbType;
        private System.Windows.Forms.Button btnAssignClear;
        private System.Windows.Forms.Button btnContentOpen;
        private System.Windows.Forms.Button btnContentSave;
        private System.Windows.Forms.Button btnContentNew;
        private System.Windows.Forms.GroupBox groupBox5;
        private System.Windows.Forms.ListBox lstResult;
        private System.Windows.Forms.Label lblStatus;
        private System.Windows.Forms.Label lblStatusP;
        private System.Windows.Forms.Label lblStatusD;
        private System.Windows.Forms.Label lblStatusC;
        private System.Windows.Forms.Timer Service_Timer;
    }
}

