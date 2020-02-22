namespace UnKnownFamilyBypassPrv8
{
    partial class LoginScreen
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(LoginScreen));
            this.KeyPlace = new System.Windows.Forms.TextBox();
            this.buykey = new System.Windows.Forms.Button();
            this.login = new System.Windows.Forms.Button();
            this.close = new System.Windows.Forms.Button();
            this.HeadTitle = new System.Windows.Forms.Label();
            this.usermessage = new System.Windows.Forms.Label();
            wrongm = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // KeyPlace
            // 
            this.KeyPlace.Location = new System.Drawing.Point(8, 104);
            this.KeyPlace.Name = "KeyPlace";
            this.KeyPlace.Size = new System.Drawing.Size(325, 20);
            this.KeyPlace.TabIndex = 0;
            this.KeyPlace.Text = "ENTER YOUR KEY HERE";
            // 
            // buykey
            // 
            this.buykey.Location = new System.Drawing.Point(8, 130);
            this.buykey.Name = "buykey";
            this.buykey.Size = new System.Drawing.Size(103, 23);
            this.buykey.TabIndex = 1;
            this.buykey.Text = "BUY KEY";
            this.buykey.UseVisualStyleBackColor = true;
            this.buykey.Click += new System.EventHandler(this.buykey_Click);
            // 
            // login
            // 
            this.login.Location = new System.Drawing.Point(117, 130);
            this.login.Name = "login";
            this.login.Size = new System.Drawing.Size(107, 23);
            this.login.TabIndex = 2;
            this.login.Text = "LOGIN";
            this.login.UseVisualStyleBackColor = true;
            this.login.Click += new System.EventHandler(this.login_Click);
            // 
            // close
            // 
            this.close.Location = new System.Drawing.Point(230, 130);
            this.close.Name = "close";
            this.close.Size = new System.Drawing.Size(103, 23);
            this.close.TabIndex = 3;
            this.close.Text = "CLOSE";
            this.close.UseVisualStyleBackColor = true;
            this.close.Click += new System.EventHandler(this.close_Click_1);
            // 
            // HeadTitle
            // 
            this.HeadTitle.AutoSize = true;
            this.HeadTitle.BackColor = System.Drawing.Color.Transparent;
            this.HeadTitle.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.HeadTitle.ForeColor = System.Drawing.Color.Lime;
            this.HeadTitle.Location = new System.Drawing.Point(5, 9);
            this.HeadTitle.Name = "HeadTitle";
            this.HeadTitle.Size = new System.Drawing.Size(191, 16);
            this.HeadTitle.TabIndex = 4;
            this.HeadTitle.Text = "[ UnKnownFamily Bypass ]";
            // 
            // usermessage
            // 
            this.usermessage.BackColor = System.Drawing.Color.Transparent;
            this.usermessage.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.usermessage.ForeColor = System.Drawing.Color.White;
            this.usermessage.Location = new System.Drawing.Point(2, 29);
            this.usermessage.Name = "usermessage";
            this.usermessage.Size = new System.Drawing.Size(301, 24);
            this.usermessage.TabIndex = 5;
            this.usermessage.Text = "Type Your Password And Click Login";
            // 
            // wrongm
            // 
            wrongm.AutoSize = true;
            wrongm.BackColor = System.Drawing.Color.Transparent;
            wrongm.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            wrongm.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(192)))), ((int)(((byte)(0)))), ((int)(((byte)(0)))));
            wrongm.Location = new System.Drawing.Point(5, 80);
            wrongm.Name = "wrongm";
            wrongm.Size = new System.Drawing.Size(0, 16);
            wrongm.TabIndex = 6;
            wrongm.Visible = false;
            // 
            // label1
            // 
            this.label1.BackColor = System.Drawing.Color.Transparent;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.ForeColor = System.Drawing.Color.White;
            this.label1.Location = new System.Drawing.Point(2, 56);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(301, 24);
            this.label1.TabIndex = 7;
            this.label1.Text = "NOTE : 1 KEY 1 PC ONLY";
            // 
            // LoginScreen
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.Black;
            this.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("$this.BackgroundImage")));
            this.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Center;
            this.ClientSize = new System.Drawing.Size(343, 159);
            this.Controls.Add(this.label1);
            this.Controls.Add(wrongm);
            this.Controls.Add(this.usermessage);
            this.Controls.Add(this.HeadTitle);
            this.Controls.Add(this.close);
            this.Controls.Add(this.login);
            this.Controls.Add(this.buykey);
            this.Controls.Add(this.KeyPlace);
            this.DoubleBuffered = true;
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "LoginScreen";
            this.Opacity = 0.95D;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "[ UnKnownFamily Bypass ]";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.LoginScreen_FormClosed);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox KeyPlace;
        private System.Windows.Forms.Button buykey;
        private System.Windows.Forms.Button login;
        private System.Windows.Forms.Button close;
        private System.Windows.Forms.Label HeadTitle;
        private System.Windows.Forms.Label usermessage;
        private System.Windows.Forms.Label wrongm;
        private System.Windows.Forms.Label label1;
    }
}

