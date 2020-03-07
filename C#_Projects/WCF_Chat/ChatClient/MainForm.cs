/*
 *  MainForm.cs
 *  
 *  Copyright 2012 Isaac Ojeda <isaacoq(at)gmail(dot)com>
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 * 
 *  This progam is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.
 * 
 */
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

using WcfChat;
using System.ServiceModel;

namespace ChatClient
{
    public partial class MainForm : Form
    {
        public MainForm()
        {
            InitializeComponent();
            lblStatus.Text = "Disconnected";
        }

        private ChannelFactory<IChatService> remoteFactory;
        private IChatService remoteProxy;
        private ChatUser clientUser;
        private bool isConnected = false;


        private void loginToolStripMenuItem_Click(object sender, EventArgs e)
        {
            try
            {
                lblStatus.Text = "Connecting...";
                LoginForm loginDialog = new LoginForm();
                loginDialog.ShowDialog(this);
                if (!String.IsNullOrEmpty(loginDialog.UserName))
                {
                    remoteFactory = new ChannelFactory<IChatService>("ChatConfig");
                    remoteProxy = remoteFactory.CreateChannel();
                    clientUser = remoteProxy.ClientConnect(loginDialog.UserName);

                    if (clientUser != null)
                    {
                        usersTimer.Enabled = true;
                        messagesTimer.Enabled = true;
                        loginToolStripMenuItem.Enabled = false;
                        btnSend.Enabled = true;
                        txtMessage.Enabled = true;
                        isConnected = true;
                        lblStatus.Text = "Connected as: " + clientUser.UserName;
                    }
                }
                else
                    lblStatus.Text = "Disconnected";
            }
            catch (Exception ex)
            {
                MessageBox.Show("An error has ocurred\nClient cannot connect\nMessage:"+ex.Message,
                    "FATAL ERROR",MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void usersTimer_Tick(object sender, EventArgs e)
        {
            List<ChatUser> listUsers = remoteProxy.GetAllUsers();
            lstUsers.DataSource = listUsers;
        }

        private void btnSend_Click(object sender, EventArgs e)
        {
            if (!String.IsNullOrEmpty(txtMessage.Text))
            {
                ChatMessage newMessage = new ChatMessage()
                {
                    Date = DateTime.Now,
                    Message = txtMessage.Text,
                    User = clientUser
                };
                remoteProxy.SendNewMessage(newMessage);
                InsertMessage(newMessage);
                txtMessage.Text = String.Empty;
            }
                
            
        }

        private void messagesTimer_Tick(object sender, EventArgs e)
        {
            List<ChatMessage> messages = remoteProxy.GetNewMessages(clientUser);

            if (messages != null)
                foreach (var message in messages)
                    InsertMessage(message);
        }

        private void InsertMessage(ChatMessage message)
        {
            txtChat.AppendText("\n" + message.User.UserName + " says (" + message.Date + "):\n" + message.Message+"\n");
        }

        private void MainForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (isConnected)
            {
                remoteProxy.SendNewMessage(new ChatMessage()
                {
                    Date = DateTime.Now,
                    Message = "i'm logged out",
                    User = clientUser
                });
                remoteProxy.RemoveUser(clientUser);
            }
        }

        private void exitToolStripMenuItem_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void txtMessage_KeyPress(object sender, KeyPressEventArgs e)
        {
            if(e.KeyChar==(char)13)            
            {
                btnSend_Click(sender, e);
                txtChat.Text = String.Empty;
            }

        }
    }
}
