/*
 *  IChatService.cs
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
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace WcfChat
{    
    [ServiceContract(SessionMode = SessionMode.Allowed)]
    public interface IChatService
    {
        [OperationContract]
        ChatUser ClientConnect(string userName);

        [OperationContract]
        List<ChatMessage> GetNewMessages(ChatUser user);

        [OperationContract]
        void SendNewMessage(ChatMessage newMessage);

        [OperationContract]
        List<ChatUser> GetAllUsers();

        [OperationContract]
        void RemoveUser(ChatUser user);        
    }

    /// <summary>
    /// 
    /// </summary>
    [DataContract]
    public class ChatMessage
    {
        private ChatUser user;
        [DataMember]
        public ChatUser User
        {
            get { return user; }
            set { user = value; }
        }
        private string message;
        [DataMember]
        public string Message
        {
            get { return message; }
            set { message = value; }
        }
        private DateTime date;
        [DataMember]
        public DateTime Date
        {
            get { return date; }
            set { date = value; }
        }
    }

    /// <summary>
    /// 
    /// </summary>
    [DataContract]
    public class ChatUser
    {
        private string userName, ipAddress, hostName;
        [DataMember]
        public string UserName
        {
            get { return userName; }
            set { userName = value; }
        }
        [DataMember]
        public string IpAddress
        {
            get { return ipAddress; }
            set { ipAddress = value; }
        }
        [DataMember]
        public string HostName
        {
            get { return hostName; }
            set { hostName = value; }
        }

        public override string ToString()
        {
            return this.UserName;
        }
    }
}
