import React, { useState, useRef, useEffect } from 'react';
import {chatApi} from "../../axiosClient";

interface Message {
    text: string;
    sender: 'user' | 'bot';
}

export const ChatWindow: React.FC = () => {
    const [messages, setMessages] = useState<Message[]>([
        { text: "Welcome to Todo Bot! How can I help you today?", sender: "bot" },
    ]);
    const [inputText, setInputText] = useState<string>('');
    const [isTyping, setIsTyping] = useState<boolean>(false);
    const [apiError, setApiError] = useState<string | null>(null);
    const messagesEndRef = useRef<HTMLDivElement>(null);
    const textAreaRef = useRef<HTMLTextAreaElement>(null);

    // Auto-scroll to bottom when new messages arrive
    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    // Auto-resize textarea as content grows
    useEffect(() => {
        const textArea = textAreaRef.current;
        if (textArea) {
            // Reset height to allow shrinking
            textArea.style.height = 'auto';
            // Set height based on scroll height (content)
            textArea.style.height = `${textArea.scrollHeight}px`;
        }
    }, [inputText]);

    // Send message to API and get response
    const sendMessageToApi = async (userMessage: string): Promise<void> => {
        setIsTyping(true);
        setApiError(null);

        try {
            const response = await chatApi.post('/simple-chat/ask', { question: userMessage });

            // API returns a string with the bot's response
            const botResponse = response.data.answer;

            setMessages(prev => [...prev, { text: botResponse, sender: "bot" }]);
        } catch (error) {
            console.error('Error sending message to API:', error);
            setMessages(prev => [...prev, { text: 'Sorry, I encountered an error. Please try again later.', sender: "bot" }]);
        } finally {
            setIsTyping(false);
        }
    };

    const handleSendMessage = async (e: React.FormEvent): Promise<void> => {
        e.preventDefault();
        if (inputText.trim() === '') return;

        // Add user message to chat
        const userMessage = inputText.trim();
        setMessages(prev => [...prev, { text: userMessage, sender: "user" }]);

        // Clear input
        setInputText('');

        // Send to API and get response
        await sendMessageToApi(userMessage);
    };

    // Handle keyboard shortcuts
    const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>): void => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            if (inputText.trim() !== '') {
                handleSendMessage(e);
            }
        }
    };

    return (
        <div className="flex flex-col h-[calc(100vh-160px)] max-w-4xl mx-auto px-4 py-6">
            {/* Chat header */}
            <div className="bg-[#00095B] rounded-t-lg p-4 text-white">
                <div className="flex items-center">
                    {/*<div className="w-10 h-10 bg-white rounded-full flex items-center justify-center mr-3">*/}
                    {/*</div>*/}
                    <div>
                        <h2 className="text-xl font-medium">Todo Bot</h2>
                        <p className="text-sm font-light">Manage your Daily Todo's</p>
                    </div>
                    <div className="ml-auto">
                        <div className="w-3 h-3 bg-green-500 rounded-full"></div>
                    </div>
                </div>
            </div>

            {/* Chat messages */}
            <div className="flex-1 bg-gray-100 p-4 overflow-y-auto">
                <div className="space-y-4">
                    {messages.map((message, index) => (
                        <div
                            key={index}
                            className={`flex ${message.sender === 'user' ? 'justify-end' : 'justify-start'}`}
                        >
                            <div
                                className={`max-w-[80%] rounded-lg p-3 ${
                                    message.sender === 'user'
                                        ? 'bg-[#00095B] text-white rounded-br-none'
                                        : 'bg-white text-gray-800 shadow-md rounded-bl-none'
                                }`}
                            >
                                <p className="text-sm md:text-base font-normal whitespace-pre-wrap">{message.text}</p>
                            </div>
                        </div>
                    ))}

                    {isTyping && (
                        <div className="flex justify-start">
                            <div className="bg-white text-gray-800 rounded-lg p-3 shadow-md rounded-bl-none max-w-[80%]">
                                <div className="flex space-x-1">
                                    <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce"></div>
                                    <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style={{animationDelay: '0.2s'}}></div>
                                    <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style={{animationDelay: '0.4s'}}></div>
                                </div>
                            </div>
                        </div>
                    )}

                    <div ref={messagesEndRef} />
                </div>
            </div>

            {/* Chat input */}
            <form onSubmit={handleSendMessage} className="bg-white p-4 border-t border-gray-200 rounded-b-lg shadow-inner">
                <div className="flex items-center space-x-3">
                    <div className="flex-1 relative">
            <textarea
                ref={textAreaRef}
                value={inputText}
                onChange={(e: React.ChangeEvent<HTMLTextAreaElement>) => setInputText(e.target.value)}
                onKeyDown={handleKeyDown}
                placeholder="Type your message here..."
                rows={1}
                className="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-bg-[#00095B] focus:border-transparent font-normal resize-none overflow-y-auto"
                style={{ maxHeight: '120px', minHeight: '44px' }}
            />
                        <div className="text-xs text-gray-400 absolute bottom-1 right-2">
                            {inputText.trim() !== '' && 'Press Enter to send'}
                        </div>
                    </div>
                    <button
                        type="submit"
                        disabled={inputText.trim() === '' || isTyping}
                        className={`bg-[#00095B] text-white p-3 rounded-md ${
                            inputText.trim() === '' || isTyping ? 'opacity-50 cursor-not-allowed' : 'hover:bg-blue-800'
                        }`}
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M14 5l7 7m0 0l-7 7m7-7H3" />
                        </svg>
                    </button>
                </div>
                <div className="mt-2 text-xs text-gray-500 font-light text-center">
                    Todo Bot can assist LLM to manage your daily tasks efficiently.
                </div>
            </form>
        </div>
    );
};
