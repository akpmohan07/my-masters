// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

contract ExtendedVault {
    address public owner;
    mapping(address => uint256) public balances;
    mapping(address => bool) public isVIP;
    mapping(address => uint256) public lastWithdrawTime;
      uint256 public constant WITHDRAWAL_DELAY = 5 minutes; // Short for testing
    uint256 public constant MAX_WITHDRAWAL = 0.01 ether;
    event Deposited(address indexed from, uint256 amount);
    event Withdrawn(address indexed to, uint256 amount);
    event VIPAdded(address indexed user);
    constructor() {
        owner = msg.sender;
    }
    modifier onlyOwner() {
        require(msg.sender == owner, "Not owner");
        _;
    }
    receive() external payable {
        balances[msg.sender] += msg.value;
        emit Deposited(msg.sender, msg.value);
    }
    function deposit() external payable {
        require(msg.value > 0, "No ETH sent");
        balances[msg.sender] += msg.value;
        emit Deposited(msg.sender, msg.value);
    }
    function withdraw(uint256 amount) external {
        require(balances[msg.sender] >= amount, "Insufficient balance");
        // Check withdrawal limits for non-VIP users
        if (!isVIP[msg.sender]) {
            require(amount <= MAX_WITHDRAWAL, "Exceeds max withdrawal");
            require(
                block.timestamp >= lastWithdrawTime[msg.sender] + WITHDRAWAL_DELAY,
                "Too soon"
            );
        }
        balances[msg.sender] -= amount;
        lastWithdrawTime[msg.sender] = block.timestamp;
        (bool success, ) = msg.sender.call{value: amount}("");
        require(success, "Transfer failed");
        emit Withdrawn(msg.sender, amount);
    }
    function addVIP(address user) external onlyOwner {
        isVIP[user] = true;
        emit VIPAdded(user);
    }
    function checkTimeUntilNextWithdraw() external view returns (uint256) {
        if (isVIP[msg.sender]) return 0;
        
        uint256 nextTime = lastWithdrawTime[msg.sender] + WITHDRAWAL_DELAY;
        if (block.timestamp >= nextTime) return 0;
        return nextTime - block.timestamp;
    }
}
