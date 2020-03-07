/*
 *  
 *  ChatService.cs
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
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]
    public class ChatService :IChatService
    {
        private ChatEngine mainEngine=new ChatEngine();

        /// <summary>
        /// 
        /// </summary>
        /// <param name="userName"></param>
        /// <returns></returns>
        public ChatUser ClientConnect(string userName)
        {
            return mainEngine.AddNewChatUser(new ChatUser() { UserName = userName });        
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="message"></param>
        /// <returns></returns>
        public List<ChatMessage> GetNewMessages(ChatUser user)
        {
            return mainEngine.GetNewMessages(user);
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="newMessage"></param>
        public void SendNewMessage(ChatMessage newMessage)
        {
            mainEngine.AddNewMessage(newMessage);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public List<ChatUser> GetAllUsers()
        {
            return mainEngine.ConectedUsers;
        }

        public void RemoveUser(ChatUser user)
        {
            mainEngine.RemoveUser(user);
        }

 
    }
}
