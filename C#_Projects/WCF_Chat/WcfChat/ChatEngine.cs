/*
 *  ChatEngine.cs
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
using System.Text;

namespace WcfChat
{
    public class ChatEngine
    {
        private List<ChatUser> conectedUsers = new List<ChatUser>();
        private Dictionary<string,List<ChatMessage>> incomingMessages= new Dictionary<string,List<ChatMessage>>();      
        public List<ChatUser> ConectedUsers
        {
            get { return conectedUsers; }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="user"></param>
        /// <returns></returns>
        public ChatUser AddNewChatUser(ChatUser user)
        {           
            var exists =
                from ChatUser e in this.ConectedUsers
                where e.UserName == user.UserName
                select e;

            if (exists.Count() == 0)
            {
                this.ConectedUsers.Add(user);
                incomingMessages.Add(user.UserName, new List<ChatMessage>() { 
                    new ChatMessage(){User=user,Message="Welcome to WPF simple chat",Date=DateTime.Now}
                });

                Console.WriteLine("New user connected: " + user);
                return user;
            }
            else
                return null;           
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="newMessage"></param>
        public void AddNewMessage(ChatMessage newMessage)
        {
            Console.WriteLine(newMessage.User.UserName+" says :"+newMessage.Message+" at "+newMessage.Date);
            
            //se envian mensajes a todos los usuarios conectados menos al que lo envia
            foreach (var user in this.ConectedUsers)
            {
                if (!newMessage.User.UserName.Equals(user.UserName))
                {
                    incomingMessages[user.UserName].Add(newMessage);                    
                }
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public List<ChatMessage> GetNewMessages(ChatUser user)
        {
            //se obtienen los nuevos mensajes
            List<ChatMessage> myNewMessages = incomingMessages[user.UserName];  
            //se borran de la "bandeja de entrada"
            incomingMessages[user.UserName]=new List<ChatMessage>();

            if (myNewMessages.Count > 0)
                return myNewMessages;
            else
                return null;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="user"></param>
        public void RemoveUser(ChatUser user)
        {
            this.ConectedUsers.RemoveAll(u=>u.UserName==user.UserName);
        }
    }
}
